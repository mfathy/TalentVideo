package me.mfathy.talent.video.data

import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.talent.video.data.mapper.data.QuestionEntityMapper
import me.mfathy.talent.video.data.store.DataStoreFactory
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.domain.repository.InterviewRepository
import javax.inject.Inject

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * InterviewRepository data implementation
 */
class QuestionsDataRepository @Inject constructor(
    private val factory: DataStoreFactory,
    private val mapper: QuestionEntityMapper
) : InterviewRepository {
    override fun getJobQuestion(qid: Int): Single<Question> {
        return factory.getCacheDataStore()
            .isQuestionCached(qid)
            .flatMap { isCached ->
                factory.getDataStore(isCached).getJobQuestionEntity(qid)
                    .flatMap {
                        factory.getCacheDataStore().saveQuestionAnswer(it)
                            .andThen(Single.just(it))
                    }
                    .map {
                        mapper.mapFromEntity(it)
                    }
            }

    }

    override fun saveQuestionAnswer(question: Question): Completable {
        return factory.getCacheDataStore().saveQuestionAnswer(mapper.mapToEntity(question))
    }

    override fun clearInterviewProgress(question: Question): Completable {
        return factory.getCacheDataStore().deleteQuestionAnswer(mapper.mapToEntity(question))
    }


}