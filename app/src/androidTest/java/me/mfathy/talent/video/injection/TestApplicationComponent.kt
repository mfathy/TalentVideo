package me.mfathy.talent.video.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import me.mfathy.movies.rater.test.TestApplication
import me.mfathy.talent.video.domain.repository.InterviewRepository
import me.mfathy.talent.video.injection.module.ViewModelsModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        TestApplicationModule::class,
        TestCacheModule::class,
        TestDataModule::class,
        ViewModelsModule::class,
        TestUiModule::class,
        TestRemoteModule::class]
)
interface TestApplicationComponent {

    fun repository(): InterviewRepository

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestApplicationComponent
    }

    fun inject(application: TestApplication)

}