package me.mfathy.talent.video.injection

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.schedulers.Schedulers
import me.mfathy.talent.video.domain.executor.ExecutionThread
import me.mfathy.talent.video.executor.ExecutionSchedulers
import me.mfathy.talent.video.injection.scope.ViewScope
import me.mfathy.talent.video.ui.question.QuestionActivity
import me.mfathy.talent.video.ui.question.QuestionFragment
import me.mfathy.talent.video.ui.review.ReviewFragment
import me.mfathy.talent.video.ui.start.StartFragment

/**
 * Dagger module to provide UI and activities dependencies.
 */
@Module
abstract class TestUiModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun providesExecutionThread(): ExecutionThread = ExecutionSchedulers(
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }


    @ViewScope
    @ContributesAndroidInjector
    abstract fun contributesQuestionActivity(): QuestionActivity

    @ViewScope
    @ContributesAndroidInjector
    abstract fun contributesQuestionFragment(): QuestionFragment

    @ViewScope
    @ContributesAndroidInjector
    abstract fun contributesStartFragment(): StartFragment

    @ViewScope
    @ContributesAndroidInjector
    abstract fun contributesReviewFragment(): ReviewFragment
}