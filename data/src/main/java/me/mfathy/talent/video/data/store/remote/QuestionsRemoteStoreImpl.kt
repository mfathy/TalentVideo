package me.mfathy.talent.video.data.store.remote

import io.reactivex.Single
import me.mfathy.talent.video.data.mapper.remote.NetworkQuestionMapper
import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore
import javax.inject.Inject

open class QuestionsRemoteStoreImpl @Inject constructor(
    private val remoteService: FakeRemoteService,
    private val mapper: NetworkQuestionMapper
) : QuestionsRemoteStore {
    override fun getJobQuestionEntity(qid: Int): Single<QuestionEntity> {
        return Single.just(mapper.mapToEntity(remoteService.getQuestion(qid)))
    }
}