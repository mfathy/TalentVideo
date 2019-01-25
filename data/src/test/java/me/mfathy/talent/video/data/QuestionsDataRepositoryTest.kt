package me.mfathy.talent.video.data

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.talent.video.data.mapper.data.QuestionEntityMapper
import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.repository.QuestionsDataStore
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore
import me.mfathy.talent.video.data.store.DataStoreFactory
import me.mfathy.talent.video.data.test.DataModelsFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit tests for QuestionsDataRepository
 */
@RunWith(MockitoJUnitRunner::class)
class QuestionsDataRepositoryTest {

    private val mockFactory: DataStoreFactory = mock()
    private val mockMapper: QuestionEntityMapper = mock()
    private val mockStore: QuestionsDataStore = mock()
    private val mockCacheStore: QuestionsCacheStore = mock()
    private val mockRemoteStore: QuestionsRemoteStore = mock()
    private val repository = QuestionsDataRepository(mockFactory, mockMapper)
    private val mapper = QuestionEntityMapper()

    @Before
    fun setup() {
        stubFactoryGetDataStore()
    }

    @Test
    fun testGetJobQuestionCompletes() {
        val question = DataModelsFactory.makeQuestionEntity()
        stubFactoryGetCacheDataStore()
        stubCacheStoreIsQuestionCached()
        stubDataStoreGetJobQuestion(question)
        stubCacheStoreSaveQuestion()

        val testObserver = repository.getJobQuestion(question.qid).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetJobQuestionCallsStore() {
        val question = DataModelsFactory.makeQuestionEntity()
        stubFactoryGetCacheDataStore()
        stubCacheStoreIsQuestionCached()
        stubDataStoreGetJobQuestion(question)
        stubCacheStoreSaveQuestion()

        repository.getJobQuestion(question.qid).test()

        verify(mockFactory, times(2)).getCacheDataStore()
        verify(mockCacheStore).isQuestionCached(any())
        verify(mockCacheStore).saveQuestionAnswer(any())
    }

    @Test
    fun testSaveQuestionAnswerCompletes() {
        val question = DataModelsFactory.makeQuestion()
        val entity = mapper.mapToEntity(question)

        stubFactoryGetCacheDataStore()
        stubCacheStoreSaveQuestion()
        stubMapperToEntity(entity)

        val testObserver = repository.saveQuestionAnswer(question).test()
        testObserver.assertComplete()
    }

    @Test
    fun testSaveQuestionAnswerCallsStore() {
        val question = DataModelsFactory.makeQuestion()
        val entity = mapper.mapToEntity(question)

        stubFactoryGetCacheDataStore()
        stubCacheStoreSaveQuestion()
        stubMapperToEntity(entity)

        repository.saveQuestionAnswer(question).test()

        verify(mockFactory, times(1)).getCacheDataStore()
        verify(mockCacheStore).saveQuestionAnswer(any())
        verify(mockMapper).mapToEntity(any())
    }

    @Test
    fun testClearInterviewProgressCompletes() {
        val question = DataModelsFactory.makeQuestion()
        val entity = mapper.mapToEntity(question)

        stubFactoryGetCacheDataStore()
        stubCacheStoreClearProgress()
        stubMapperToEntity(entity)

        val testObserver = repository.clearInterviewProgress(question).test()
        testObserver.assertComplete()
    }

    @Test
    fun testClearInterviewProgressCallsStore() {
        val question = DataModelsFactory.makeQuestion()
        val entity = mapper.mapToEntity(question)

        stubFactoryGetCacheDataStore()
        stubCacheStoreClearProgress()
        stubMapperToEntity(entity)

        repository.clearInterviewProgress(question).test()

        verify(mockFactory, times(1)).getCacheDataStore()
        verify(mockCacheStore).deleteQuestionAnswer(any())
        verify(mockMapper).mapToEntity(any())
    }

    private fun stubCacheStoreClearProgress() {
        whenever(mockCacheStore.deleteQuestionAnswer(any())).thenReturn(Completable.complete())
    }

    private fun stubFactoryGetDataStore() {
        whenever(mockFactory.getDataStore(any())).thenReturn(mockStore)
    }

    private fun stubCacheStoreSaveQuestion() {
        whenever(mockCacheStore.saveQuestionAnswer(any())).thenReturn(Completable.complete())
    }

    private fun stubDataStoreGetJobQuestion(question: QuestionEntity) {
        whenever(mockStore.getJobQuestionEntity(any())).thenReturn(Single.just(question))
    }

    private fun stubCacheStoreIsQuestionCached() {
        whenever(mockCacheStore.isQuestionCached(any())).thenReturn(Single.just(true))
    }

    private fun stubFactoryGetCacheDataStore() {
        whenever(mockFactory.getCacheDataStore()).thenReturn(mockCacheStore)
    }

    private fun stubMapperToEntity(entity: QuestionEntity) {
        whenever(mockMapper.mapToEntity(any())).thenReturn(entity)
    }
}