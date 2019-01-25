package me.mfathy.talent.video.ui.question

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import me.mfathy.talent.video.test.TestFactory
import me.mfathy.talent.video.domain.interactor.questions.ClearInterviewProgress
import me.mfathy.talent.video.domain.interactor.questions.GetJobQuestion
import me.mfathy.talent.video.domain.interactor.questions.SaveQuestionAnswer
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.model.DataState
import me.mfathy.talent.video.test.TestModelsFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Captor
import kotlin.test.assertEquals

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for QuestionViewModel
 */
@RunWith(JUnit4::class)
class QuestionViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetQuestionUseCase: GetJobQuestion = mock()
    private val mockSaveQuestionUseCase: SaveQuestionAnswer = mock()
    private val mockClearProgressUseCase: ClearInterviewProgress = mock()

    private var viewModel = QuestionViewModel(
        mockGetQuestionUseCase,
        mockSaveQuestionUseCase,
        mockClearProgressUseCase
    )

    @Captor
    val captor = argumentCaptor<DisposableSingleObserver<Question>>()


    @Test
    fun testFetchQuestionExecutesUseCase() {
        val question = TestModelsFactory.makeQuestion()
        stubGetQuestionUseCase(question)

        viewModel.fetchQuestion(question.qid)

        verify(mockGetQuestionUseCase, times(1)).execute(any())
    }

    @Test
    fun testSaveQuestionAnswerExecutesUseCase() {
        val question = TestModelsFactory.makeQuestion()
        stubSaveQuestionUseCase(question)

        viewModel.saveQuestionAnswer(question)

        verify(mockSaveQuestionUseCase, times(1)).execute(any())
    }

    @Test
    fun testClearInterViewProgressExecutesUseCase() {
        val question = TestModelsFactory.makeQuestion()
        stubClearInterViewUseCase(question)

        viewModel.clearInterViewProgress(question)

        verify(mockClearProgressUseCase, times(1)).execute(any())

    }

    private fun stubGetQuestionUseCase(question: Question) {
        whenever(mockGetQuestionUseCase.execute(any())).thenReturn(Single.just(question))
    }

    private fun stubSaveQuestionUseCase(question: Question) {
        whenever(mockSaveQuestionUseCase.execute(any())).thenReturn(Completable.complete())
    }

    private fun stubClearInterViewUseCase(question: Question) {
        whenever(mockClearProgressUseCase.execute(any())).thenReturn(Completable.complete())
    }
}