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
 * ClearInterviewProgress use case clears all interview progress for a candidate.
 */
open class ClearInterviewProgress @Inject constructor(
    private val repository: InterviewRepository,
    executor: ExecutionThread
) : CompletableUseCase<ClearInterviewProgress.Params>(executor) {

    public override fun buildUseCaseCompletable(params: Params?): Completable {
        if (params == null) throw IllegalArgumentException("Params can't be null!")
        return repository.clearInterviewProgress(params.question)
    }

    class Params(val question: Question) {
        companion object {
            fun forClearInterviewProgress(question: Question): Params = Params(question)
        }
    }
}