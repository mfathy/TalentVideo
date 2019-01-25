package me.mfathy.talent.video.data.store.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import me.mfathy.talent.video.data.mapper.cache.CachedQuestionMapper
import me.mfathy.talent.video.data.store.cache.db.QuestionsDatabase
import me.mfathy.talent.video.data.test.DataModelsFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for QuestionsCacheStoreImpl
 */
@RunWith(RobolectricTestRunner::class)
class QuestionsCacheStoreImplTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database = Room.inMemoryDatabaseBuilder(
        RuntimeEnvironment.application.applicationContext,
        QuestionsDatabase::class.java
    )
        .allowMainThreadQueries()
        .build()

    private val mapper = CachedQuestionMapper()
    private val store = QuestionsCacheStoreImpl(database, mapper)

    @Test
    fun testIsQuestionCachedReturnFalse() {
        val question = DataModelsFactory.makeQuestionEntity()
        store.saveQuestionAnswer(question).test()

        val testObserver = store.isQuestionCached(question.qid).test()
        testObserver.assertValue(true)
    }

    @Test
    fun testSaveQuestionAnswerCompletes() {
        val question = DataModelsFactory.makeQuestionEntity()
        val testObserver = store.saveQuestionAnswer(question).test()

        testObserver.assertComplete()
    }

    @Test
    fun tesDeleteQuestionAnswerCompletes() {
        val question = DataModelsFactory.makeQuestionEntity()
        store.saveQuestionAnswer(question).test()

        val testObserver = store.deleteQuestionAnswer(question).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetJobQuestionEntityCompletes() {
        val question = DataModelsFactory.makeQuestionEntity()
        store.saveQuestionAnswer(question).test()

        val testObserver = store.getJobQuestionEntity(qid = question.qid).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetJobQuestionEntityReturnData() {
        val question = DataModelsFactory.makeQuestionEntity()
        store.saveQuestionAnswer(question).test()

        val testObserver = store.getJobQuestionEntity(qid = question.qid).test()
        testObserver.assertValue(question)
    }
}