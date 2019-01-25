package me.mfathy.talent.video.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.talent.video.domain.model.Question

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * InterviewRepository is the contract for question interview process events.
 */
interface InterviewRepository {

    /**
     * Returns a Question that should be answered by a candidate.
     * @return an Single that emits a question, otherwise error.
     */
    fun getJobQuestion(qid: Int): Single<Question>

    /**
     * Saves an answer of a question of job application.
     * @param question the question to be answered.
     * @return a Completable that emits completed or error.
     */
    fun saveQuestionAnswer(question: Question): Completable

    /**
     * Clears all interview process progress using its job code.
     * @param question the question to clear it's progress.
     * @return a Completable that emits completed or error.
     */
    fun clearInterviewProgress(question: Question): Completable

}