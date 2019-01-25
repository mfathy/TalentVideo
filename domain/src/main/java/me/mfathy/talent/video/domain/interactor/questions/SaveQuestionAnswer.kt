package me.mfathy.talent.video.domain.interactor.questions

import io.reactivex.Completable
import me.mfathy.talent.video.domain.executor.ExecutionThread
import me.mfathy.talent.video.domain.interactor.base.CompletableUseCase
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.domain.repository.InterviewRepository
import javax.inject.Inject

/**
 * Created by Mohammed Fathy on 21/01/2019.
 * dev.mfathy@gmail.com
 *
 * SaveQuestionAnswer use case to save a candidate answer to a question.
 */
open class SaveQuestionAnswer @Inject constructor(
    private val repository: InterviewRepository,
    executor: ExecutionThread
) : CompletableUseCase<SaveQuestionAnswer.Params>(executor) {
    public override fun buildUseCaseCompletable(params: Params?): Completable {
        if (params == null) throw IllegalArgumentException("Params can't be null!")
        return repository.saveQuestionAnswer(params.question)
    }

    class Params(val question: Question) {
        companion object {
            fun forUpdateQuestionAnswer(question: Question): Params = Params(question)
        }
    }
}