package me.mfathy.talent.video.ui.question

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.question_fragment.*
import me.mfathy.talent.video.BuildConfig
import me.mfathy.talent.video.R
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.injection.factory.ViewModelFactory
import me.mfathy.talent.video.model.DataState
import javax.inject.Inject

/**
 * QuestionFragment to handle showing a question for the candidate and record his answer.
 */
class QuestionFragment : FrontCameraFragment() {

    private lateinit var mQuestion: Question

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.question_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val questionId = arguments?.getInt("questionId")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuestionViewModel::class.java)

        //  Live data observing.
        observerSaveAnswerLiveData(questionId)
        observeClearProgressLiveData()
        observeGetQuestionLiveData()

        //  Listeners
        recordButton.setOnClickListener {
            if (isRecording) {
                isRecording = false
                stopRecordSession()
                (it as Button).text = getString(R.string.answer)
                viewModel.saveQuestionAnswer(mQuestion)
            } else {
                isRecording = true
                startRecordSession(mQuestion.qid.toString())
                (it as Button).text = getString(R.string.done)
            }
        }

        //  Fetch the question.
        questionId?.let {
            viewModel.fetchQuestion(it)
        }
    }

    override fun onResume() {
        super.onResume()

        startBackgroundThread()
        if (previewTextureView.isAvailable)
            openCamera()
        else
            previewTextureView.surfaceTextureListener = surfaceListener
    }

    override fun onPause() {
        stopDemoTimer()
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    //region Observers
    private fun observeClearProgressLiveData() {
        viewModel.isProgressClearedLiveData().observe(this, Observer {
            it?.let { isCleared ->
                if (isCleared) {
                } else {
                }
            }
        })
    }

    private fun observerSaveAnswerLiveData(questionId: Int?) {
        viewModel.isAnswerSavedLiveData().observe(this, Observer {
            it?.let { isSaved ->
                if (isSaved) {
                    view?.let { fragmentView ->
                        questionId?.let {
                            navigateToReviewFragment(questionId, fragmentView)
                        }
                    }
                }
            }
        })
    }

    private fun observeGetQuestionLiveData() {
        viewModel.getQuestionLiveData().observe(this, Observer {
            it?.let { state -> handleGetQuestionState(state) }
        })
    }
    //endregion

    //region Handlers
    private fun navigateToReviewFragment(questionId: Int, fragmentView: View) {
        val action = QuestionFragmentDirections.actionReviewAnswerFragment(questionId)
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.answer_question, true).build()
        Navigation.findNavController(fragmentView).navigate(action, navOptions)
    }

    private fun handleGetQuestionState(state: DataState) {
        when (state) {
            is DataState.OnLoading -> {
            }
            is DataState.OnError -> {
                showErrorMessage(state.error)
            }
            is DataState.OnSuccess<*> -> {
                state.data?.let {
                    if (it is Question) {
                        mQuestion = it
                        questionText.text = it.question

                        startDemoTimer(45, true)
                    }
                }
            }
        }
    }

    private fun showErrorMessage(error: Throwable) {
        view?.let {
            Snackbar.make(it, error.localizedMessage, Snackbar.LENGTH_LONG).show()
        }

        if (BuildConfig.DEBUG) error.printStackTrace()

    }

    fun startRecordSession(videoId: String) {
        recordSession(videoId)
        stopDemoTimer()
        startDemoTimer(45, false)
    }

    fun stopRecordSession() {
        stopDemoTimer()
        stopMediaRecorder()
        previewSession()
        mQuestion.answerVideoUrl = currentVideoFilePath
        mQuestion.isAnswered = true
    }
    //endregion

    //region Record & Prepare Timer
    private lateinit var demoTimer: CountDownTimer

    private fun startDemoTimer(seconds: Long, isDemoMode: Boolean) {
        demoTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onFinish() {
                if (isDemoMode) {
                    isRecording = true
                    startRecordSession(mQuestion.qid.toString())
                    recordButton.text = getString(R.string.done)
                } else {
                    isRecording = false
                    stopRecordSession()
                    recordButton.text = getString(R.string.answer)
                    viewModel.saveQuestionAnswer(mQuestion)
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000
                if (isDemoMode) chronometer.text = "Prepare: 00:$secondsUntilFinished"
                else chronometer.text = "Recording: 00:$secondsUntilFinished"
            }
        }

        demoTimer.start()
    }

    private fun stopDemoTimer() {
        demoTimer.cancel()
    }
    //endregion
}
