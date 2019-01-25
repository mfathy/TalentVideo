package me.mfathy.talent.video.domain.interactor.questions

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import me.mfathy.talent.video.domain.executor.ExecutionThread
import me.mfathy.talent.video.domain.repository.InterviewRepository
import me.mfathy.talent.video.domain.test.DomainModelsFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mohammed Fathy on 21/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for [SaveQuestionAnswer]
 */
@RunWith(MockitoJUnitRunner::class)
class SaveQuestionAnswerTest {
    private lateinit var useCase: SaveQuestionAnswer

    @Mock
    lateinit var mockRepository: InterviewRepository

    @Mock
    lateinit var mockExecutor: ExecutionThread

    @Before
    fun setUp() {
        useCase = SaveQuestionAnswer(mockRepository, mockExecutor)

        stubRxObserver()
        stubRxSubscriber()
    }

    @Test
    fun testUpdateQuestionAnswerCompletes() {
        stubUpdateQuestionAnswer(Completable.complete())
        val question = DomainModelsFactory.makeQuestion()
        val testObserver =
            useCase.execute(SaveQuestionAnswer.Params.forUpdateQuestionAnswer(question)).test()

        testObserver.assertComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUpdateQuestionAnswerThrowsException() {
        useCase.execute(null).test()
    }

    @Test
    fun testUpdateQuestionAnswerCallsRepository() {
        stubUpdateQuestionAnswer(Completable.complete())

        val question = DomainModelsFactory.makeQuestion()

        useCase.execute(SaveQuestionAnswer.Params.forUpdateQuestionAnswer(question)).test()

        verify(mockRepository).saveQuestionAnswer(any())
    }

    private fun stubUpdateQuestionAnswer(complete: Completable?) {
        whenever(mockRepository.saveQuestionAnswer(any())).thenReturn(complete)
    }

    private fun stubRxObserver() {
        whenever(mockExecutor.observer).thenReturn(Schedulers.trampoline())
    }

    private fun stubRxSubscriber() {
        whenever(mockExecutor.subscriber).thenReturn(Schedulers.trampoline())
    }
}