package me.mfathy.talent.video.data.store.cache.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import me.mfathy.talent.video.data.store.cache.db.QuestionsDatabase
import me.mfathy.talent.video.data.test.DataModelsFactory
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for CachedQuestionDao
 */
@RunWith(RobolectricTestRunner::class)
class CachedQuestionDaoTest{
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database = Room.inMemoryDatabaseBuilder(
        RuntimeEnvironment.application.applicationContext,
        QuestionsDatabase::class.java
    )
        .allowMainThreadQueries()
        .build()

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetCachedQuestionReturnsData() {
        val cachedQuestion = DataModelsFactory.makeCachedQuestion()
        database.cachedQuestions().insertQuestion(cachedQuestion)

        val testObserver = database.cachedQuestions().getCachedQuestion(cachedQuestion.qid).test()
        testObserver.assertValue(cachedQuestion)
    }

    @Test
    fun testIsQuestionCachedReturnsCount() {
        val cachedQuestion = DataModelsFactory.makeCachedQuestion()
        database.cachedQuestions().insertQuestion(cachedQuestion)

        val testObserver = database.cachedQuestions().isQuestionCached(cachedQuestion.qid).test()
        testObserver.assertValue(1)
    }

    @Test
    fun testUpdateQuestionUpdatesData(){

        val cachedQuestion = DataModelsFactory.makeCachedQuestion()
        database.cachedQuestions().insertQuestion(cachedQuestion)

        val updatedQuestion = cachedQuestion.copy(isAnswered = false, answerVideoUrl = "-")

        database.cachedQuestions().updateQuestion(updatedQuestion)

        val testObserver = database.cachedQuestions().getCachedQuestion(cachedQuestion.qid).test()
        testObserver.assertValue(updatedQuestion)

    }

    @Test
    fun testDeleteQuestionDeletesData(){
        val cachedQuestion = DataModelsFactory.makeCachedQuestion()
        database.cachedQuestions().insertQuestion(cachedQuestion)
        database.cachedQuestions().deleteQuestion(cachedQuestion)

        val testObserver = database.cachedQuestions().isQuestionCached(cachedQuestion.qid).test()
        testObserver.assertValue(0)

    }
}