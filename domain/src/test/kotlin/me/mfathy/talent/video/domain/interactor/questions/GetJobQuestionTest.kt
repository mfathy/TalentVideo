package me.mfathy.talent.video.domain.interactor.questions

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.mfathy.talent.video.domain.executor.ExecutionThread
import me.mfathy.talent.video.domain.model.Question
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
 * Unit test for [GetJobQuestion]
 */
@RunWith(MockitoJUnitRunner::class)
class GetJobQuestionTest {
    private lateinit var useCase: GetJobQuestion

    @Mock
    lateinit var mockRepository: InterviewRepository

    @Mock
    lateinit var mockExecutor: ExecutionThread

    @Before
    fun setUp() {
        useCase = GetJobQuestion(mockRepository, mockExecutor)

        stubRxObserver()
        stubRxSubscriber()
    }


    @Test
    fun testGetJobQuestionCompletes() {
        val question = DomainModelsFactory.makeQuestion()
        stubGetJobQuestion(Single.just(question))
        val testObserver =
            useCase.execute(GetJobQuestion.Params.forGetJobQuestion(question.qid)).test()

        testObserver.assertComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGetJobQuestionThrowsException() {
        useCase.execute(null).test()
    }

    @Test
    fun testGetJobQuestionCallsRepository() {
        val question = DomainModelsFactory.makeQuestion()
        stubGetJobQuestion(Single.just(question))

        useCase.execute(GetJobQuestion.Params.forGetJobQuestion(question.qid)).test()

        verify(mockRepository).getJobQuestion(any())
    }

    private fun stubGetJobQuestion(single: Single<Question>) {
        whenever(mockRepository.getJobQuestion(any())).thenReturn(single)
    }

    private fun stubRxObserver() {
        whenever(mockExecutor.observer).thenReturn(Schedulers.trampoline())
    }

    private fun stubRxSubscriber() {
        whenever(mockExecutor.subscriber).thenReturn(Schedulers.trampoline())
    }

}