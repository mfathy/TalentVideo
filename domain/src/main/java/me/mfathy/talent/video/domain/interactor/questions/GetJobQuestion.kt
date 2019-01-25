package me.mfathy.talent.video.domain.interactor.questions

import io.reactivex.Single
import me.mfathy.talent.video.domain.executor.ExecutionThread
import me.mfathy.talent.video.domain.interactor.base.SingleUseCase
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.domain.repository.InterviewRepository
import javax.inject.Inject

/**
 * Created by Mohammed Fathy on 21/01/2019.
 * dev.mfathy@gmail.com
 *
 * GetJobQuestion use case to get a job question.
 */

open class GetJobQuestion @Inject constructor(
    private val repository: InterviewRepository,
    executor: ExecutionThread
) : SingleUseCase<Question, GetJobQuestion.Params>(executor) {
    public override fun buildUseCaseSingle(params: Params?): Single<Question> {
        if (params == null) throw IllegalArgumentException("Params can't be null!")
        return repository.getJobQuestion(params.qid)
    }

    class Params(val qid: Int) {
        companion object {
            fun forGetJobQuestion(qid: Int): Params {
                return Params(qid)
            }
        }
    }
}