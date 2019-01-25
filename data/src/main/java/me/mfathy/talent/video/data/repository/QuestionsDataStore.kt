package me.mfathy.talent.video.data.repository

import io.reactivex.Single
import me.mfathy.talent.video.data.model.QuestionEntity

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * QuestionsDataStore is the contract for questions data store.
 */
interface QuestionsDataStore {

    /**
     * Returns a Question that should be answered by a candidate.
     * @param qid of the question
     * @return an Single that emits a question, otherwise error.
     */
    fun getJobQuestionEntity(qid: Int): Single<QuestionEntity>
}