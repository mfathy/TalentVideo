package me.mfathy.talent.video.ui.start

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.start_fragment.*
import me.mfathy.talent.video.BuildConfig
import me.mfathy.talent.video.R
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.injection.factory.ViewModelFactory
import me.mfathy.talent.video.model.DataState
import me.mfathy.talent.video.ui.question.QuestionViewModel
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

/**
 * StartFragment to start the video interview questionnaire.
 */
class StartFragment : DaggerFragment() {

    companion object {
        const val TAG = "StartFragment"
        fun newInstance() = StartFragment()
    }

    private lateinit var mQuestion: Question
    private var questionId: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: QuestionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuestionViewModel::class.java)

        viewModel.getQuestionLiveData().observe(this, Observer {
            it?.let { state -> handleGetQuestionState(state) }
        })

        answer_btn.setOnClickListener { view ->
            checkForPermissions(view)
        }

        //  Return random questing every time the user ask for a new question.
        questionId = ThreadLocalRandom.current().nextInt(0, 4)
        Log.d(TAG, questionId.toString())
        viewModel.fetchQuestion(questionId)
    }

    private fun handleGetQuestionState(state: DataState) {
        when (state) {
            is DataState.OnLoading -> {
            }
            is DataState.OnError -> {
                showErrorMessage(state.error)
            }
            is DataState.OnSuccess<*> -> {
                state.data?.let { question ->
                    if (question is Question) {
                        mQuestion = question
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

    private fun checkForPermissions(v: View) {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.areAllPermissionsGranted()?.let { isAllGranted ->
                        if (isAllGranted) {
                            if (!mQuestion.isAnswered) {
                                view?.let {
                                    navigateToQuestionFragment(it)
                                }
                            } else {
                                view?.let {
                                    navigateToReviewFragment(it)
                                }
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }

    private fun navigateToReviewFragment(it: View) {
        val actionReview = StartFragmentDirections.actionReviewAnswerFragment(questionId, true)
        val navOption = NavOptions.Builder().setPopUpTo(R.id.review_answer, true).build()
        Navigation.findNavController(it).navigate(actionReview, navOption)
    }

    private fun navigateToQuestionFragment(it: View) {
        val actionStart = StartFragmentDirections.actionAnswerQuestionFragment(questionId)
        val navOption = NavOptions.Builder().setPopUpTo(R.id.answer_question, true).build()
        Navigation.findNavController(it).navigate(actionStart, navOption)
    }
}
