package me.mfathy.talent.video.ui.question


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import me.mfathy.movies.rater.test.TestApplication
import me.mfathy.talent.video.R
import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.factory.UiTestModelsFactory
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Espresso test for Question activity.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class QuestionActivityTest {

    @Rule
    @JvmField
    val mActivityTestRule = ActivityTestRule<QuestionActivity>(QuestionActivity::class.java, false, false)

    private val question = UiTestModelsFactory.makeQuestion()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext


    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )!!


    @Before
    fun setUp() {
        stubMoviesRepositoryClearQuestion()
        stubMoviesRepositorySaveQuestion()
        stubMoviesRepositoryGetQuestion(Single.just(question))
    }

    private fun stubMoviesRepositoryGetQuestion(single: Single<Question>) {
        whenever(TestApplication.appComponent().repository().getJobQuestion(com.nhaarman.mockito_kotlin.any()))
            .thenReturn(single)

    }

    private fun stubMoviesRepositorySaveQuestion() {
        whenever(TestApplication.appComponent().repository().saveQuestionAnswer(com.nhaarman.mockito_kotlin.any()))
            .thenReturn(Completable.complete())
    }

    private fun stubMoviesRepositoryClearQuestion() {
        whenever(TestApplication.appComponent().repository().clearInterviewProgress(com.nhaarman.mockito_kotlin.any()))
            .thenReturn(Completable.complete())
    }


    @Test
    fun questionActivityTest() {

        mActivityTestRule.launchActivity(null)

        val appCompatButton = onView(
            allOf(withId(R.id.answer_btn), withText("Answer Question"), isDisplayed())
        )
        appCompatButton.perform(click())

        Thread.sleep(500)

        val textView = onView(
            allOf(withId(R.id.questionText), isDisplayed())
        )
        textView.check(matches(withText(question.question)))


        Thread.sleep(500)

        val button = onView(
            allOf(withId(R.id.recordButton), isDisplayed())
        )
        button.check(matches(isDisplayed()))
        button.check(matches(withText("Answer")))

        button.perform(click())

        Thread.sleep(500)

        button.check(matches(withText("Done")))

    }
}
