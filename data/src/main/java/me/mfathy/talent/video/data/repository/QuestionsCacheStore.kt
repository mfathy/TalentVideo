package me.mfathy.talent.video.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.talent.video.data.model.QuestionEntity

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * QuestionsCacheStore is the contract for questions cache store.
 */
interface QuestionsCacheStore : QuestionsDataStore {

    /**
     * Returns A single that emits true if the question is already cached.
     * @param qid of the question.
     */
    fun isQuestionCached(qid: Int): Single<Boolean>

    /**
     * Saves an answer of a question of job application.
     * @param question the question to be answered.
     * @return a Completable that emits completed or error.
     */
    fun saveQuestionAnswer(question: QuestionEntity): Completable

    /**
     * Clears all interview process progress using its job code.
     * @param question the question to clear it's progress.
     * @return a Completable that emits completed or error.
     */
    fun deleteQuestionAnswer(question: QuestionEntity): Completable
}