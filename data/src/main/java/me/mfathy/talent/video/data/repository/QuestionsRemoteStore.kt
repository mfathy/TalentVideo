package me.mfathy.talent.video.data.repository

import io.reactivex.Single
import me.mfathy.talent.video.data.model.QuestionEntity

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * QuestionsRemoteStore is the contract for questions remote store.
 */
interface QuestionsRemoteStore : QuestionsDataStore {

    /**
     * Returns a Question that should be answered by a candidate.
     * @return an Single that emits a question, otherwise error.
     */
    override fun getJobQuestionEntity(qid: Int): Single<QuestionEntity>
}