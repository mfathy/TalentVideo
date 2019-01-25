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
 * Unit test for [ClearInterviewProgress]
 */
@RunWith(MockitoJUnitRunner::class)
class ClearInterviewProgressTest {
    private lateinit var useCase: ClearInterviewProgress

    @Mock
    lateinit var mockRepository: InterviewRepository

    @Mock
    lateinit var mockExecutor: ExecutionThread

    @Before
    fun setUp() {
        useCase = ClearInterviewProgress(mockRepository, mockExecutor)

        stubRxObserver()
        stubRxSubscriber()
    }

    @Test
    fun testClearInterviewProcessCompletes() {
        stubClearInterviewProcess(Completable.complete())

        val question = DomainModelsFactory.makeQuestion()
        val testObserver =
            useCase.execute(ClearInterviewProgress.Params.forClearInterviewProgress(question)).test()

        testObserver.assertComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testClearInterviewProcessThrowsException() {
        useCase.execute(null).test()
    }

    @Test
    fun testClearInterviewProcessCallsRepository() {
        stubClearInterviewProcess(Completable.complete())

        val question = DomainModelsFactory.makeQuestion()

        useCase.execute(ClearInterviewProgress.Params.forClearInterviewProgress(question)).test()

        verify(mockRepository).clearInterviewProgress(any())
    }

    private fun stubRxObserver() {
        whenever(mockExecutor.observer).thenReturn(Schedulers.trampoline())
    }

    private fun stubRxSubscriber() {
        whenever(mockExecutor.subscriber).thenReturn(Schedulers.trampoline())
    }

    private fun stubClearInterviewProcess(complete: Completable?) {
        whenever(mockRepository.clearInterviewProgress(any())).thenReturn(complete)
    }
}