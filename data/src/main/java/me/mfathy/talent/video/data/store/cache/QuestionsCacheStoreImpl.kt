package me.mfathy.talent.video.data.store.cache

import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.talent.video.data.mapper.cache.CachedQuestionMapper
import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.store.cache.db.QuestionsDatabase
import javax.inject.Inject

open class QuestionsCacheStoreImpl @Inject constructor(
    private val database: QuestionsDatabase,
    private val mapper: CachedQuestionMapper
) : QuestionsCacheStore {
    override fun isQuestionCached(qid: Int): Single<Boolean> {
        return database.cachedQuestions().isQuestionCached(qid)
            .map { it > 0 }
    }

    override fun saveQuestionAnswer(question: QuestionEntity): Completable {
        return Completable.defer {
            database.cachedQuestions().insertQuestion(mapper.mapFromEntity(question))
            Completable.complete()
        }
    }

    override fun deleteQuestionAnswer(question: QuestionEntity): Completable {
        return Completable.defer {
            database.cachedQuestions().deleteQuestion(mapper.mapFromEntity(question))
            Completable.complete()
        }
    }

    override fun getJobQuestionEntity(qid: Int): Single<QuestionEntity> {
        return database.cachedQuestions().getCachedQuestion(qid).map {
            mapper.mapToEntity(it)
        }
    }
}