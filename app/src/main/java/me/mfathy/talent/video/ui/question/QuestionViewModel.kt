package me.mfathy.talent.video.ui.question

import androidx.lifecycle.MutableLiveData
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import me.mfathy.talent.video.domain.interactor.questions.ClearInterviewProgress
import me.mfathy.talent.video.domain.interactor.questions.GetJobQuestion
import me.mfathy.talent.video.domain.interactor.questions.SaveQuestionAnswer
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.model.DataState
import javax.inject.Inject

/**
 *  ViewModel for fragments [QuestionFragment, ReviewFragment, StartFragmet]
 */
class QuestionViewModel @Inject constructor(
    private val getJobQuestionUseCase: GetJobQuestion,
    private val saveQuestionAnswer: SaveQuestionAnswer,
    private val clearInterviewProgress: ClearInterviewProgress
) : BaseViewModel() {

    private val questionsLiveData: MutableLiveData<DataState> = MutableLiveData()
    private val answerSavedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val progressClearedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        clearDisposables()
        super.onCleared()
    }

    fun getQuestionLiveData(): MutableLiveData<DataState> = questionsLiveData
    fun isAnswerSavedLiveData(): MutableLiveData<Boolean> = answerSavedLiveData
    fun isProgressClearedLiveData(): MutableLiveData<Boolean> = progressClearedLiveData

    /**
     * Fetches a job question.
     */
    fun fetchQuestion(qid: Int) {
        val params = GetJobQuestion.Params.forGetJobQuestion(qid)
        getJobQuestionUseCase.execute(params).subscribeWith(GetQuestionObserver()).add()
    }

    /**
     * Saves an answer to a job question.
     */
    fun saveQuestionAnswer(question: Question) {
        val params = SaveQuestionAnswer.Params.forUpdateQuestionAnswer(question)
        saveQuestionAnswer.execute(params).subscribeWith(SaveAnswerObserver()).add()
    }

    /**
     * Clears interview progress for a job.
     */
    fun clearInterViewProgress(question: Question) {
        val params = ClearInterviewProgress.Params.forClearInterviewProgress(question)
        clearInterviewProgress.execute(params).subscribeWith(ClearProgressObserver()).add()
    }

    //region Observers
    inner class SaveAnswerObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            answerSavedLiveData.postValue(true)
        }

        override fun onError(e: Throwable) {
            answerSavedLiveData.postValue(false)
        }

    }

    inner class ClearProgressObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            progressClearedLiveData.postValue(true)
        }

        override fun onError(e: Throwable) {
            progressClearedLiveData.postValue(true)
        }

    }

    inner class GetQuestionObserver : DisposableSingleObserver<Question>() {
        override fun onSuccess(question: Question) {
            questionsLiveData.postValue(DataState.OnSuccess(question))
        }

        override fun onError(e: Throwable) {
            questionsLiveData.postValue(DataState.OnError(e))
        }
    }
    //endregion
}
