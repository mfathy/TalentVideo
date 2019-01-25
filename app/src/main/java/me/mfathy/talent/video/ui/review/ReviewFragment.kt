package me.mfathy.talent.video.ui.review

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.review_fragment.*
import me.mfathy.talent.video.BuildConfig
import me.mfathy.talent.video.R
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.injection.factory.ViewModelFactory
import me.mfathy.talent.video.model.DataState
import me.mfathy.talent.video.ui.question.QuestionViewModel
import java.io.File
import javax.inject.Inject

/**
 * ReviewFragment preview the user answer.
 */
class ReviewFragment : DaggerFragment() {

    companion object {
        fun newInstance() = ReviewFragment()
    }

    private lateinit var mQuestion: Question

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: QuestionViewModel

    private var reviewTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val questionId = arguments?.getInt("questionId")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuestionViewModel::class.java)

        viewModel.getQuestionLiveData().observe(this, Observer {
            it?.let { state -> handleGetQuestionState(state) }
        })

        retryButton.setOnClickListener { view ->
            view?.let {
                viewModel.clearInterViewProgress(mQuestion)
                val navOption = NavOptions.Builder().setPopUpTo(R.id.review_answer, true).build()
                val action = ReviewFragmentDirections.actionAnswerQuestionFragment(mQuestion.qid)
                Navigation.findNavController(it).navigate(action, navOption)
            }
        }

        doneButton.setOnClickListener {
            navigateToStartScreen()
        }

        questionId?.let {
            viewModel.fetchQuestion(it)
        }
    }

    override fun onPause() {
        stopReviewTimer()
        super.onPause()
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
                        answerTextReview.text = it.question
                        if (it.isAnswered)
                            isAnsweredImageView.visibility = View.VISIBLE
                        else
                            isAnsweredImageView.visibility = View.INVISIBLE

                        val startWithNoTimer = arguments?.getBoolean("noTimer")
                        startWithNoTimer?.let {
                            if (!startWithNoTimer) startReviewTimer(60)
                        }
                        startExoPlayer()
                    }
                }
            }
        }
    }

    private fun startReviewTimer(seconds: Long) {
        reviewTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onFinish() {
                navigateToStartScreen()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000
                reviewTimerText.text = "00:$secondsUntilFinished"
            }
        }

        reviewTimer?.start()
    }

    private fun stopReviewTimer() {
        if (reviewTimer != null) reviewTimer?.cancel()
    }

    private fun navigateToStartScreen() {
        view?.let { fragmentView ->
            val navOption = NavOptions.Builder().setPopUpTo(R.id.review_answer, true).build()
            val action = ReviewFragmentDirections.actionStartScreenFragment()
            Navigation.findNavController(fragmentView).navigate(action, navOption)
        }
    }

    private fun showErrorMessage(error: Throwable) {
        view?.let {
            Snackbar.make(it, error.localizedMessage, Snackbar.LENGTH_LONG).show()
        }

        if (BuildConfig.DEBUG) error.printStackTrace()
    }

    //region ExoPlayer
    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }

    private val trackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    private val trackSelection by lazy {
        DefaultTrackSelector(trackSelectionFactory)
    }

    private val simpleExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(context, trackSelection)
    }

    private val applicationName by lazy {
        context?.applicationInfo?.loadLabel(context?.packageManager).toString()
    }

    private val dataSourceFactory by lazy {
        DefaultDataSourceFactory(context, Util.getUserAgent(context, applicationName))
    }

    private val videoMediaSource by lazy {
        ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.fromFile(File(mQuestion.answerVideoUrl)))
    }

    private fun startExoPlayer() {
        previewPlayer.controllerAutoShow = false
        previewPlayer.player = simpleExoPlayer
        simpleExoPlayer.prepare(videoMediaSource)
        simpleExoPlayer.playWhenReady = true
    }

    private fun stopExoPlayer() {
        simpleExoPlayer.stop()
        simpleExoPlayer.release()
    }

    override fun onStop() {
        stopExoPlayer()
        super.onStop()
    }
    //endregion
}
