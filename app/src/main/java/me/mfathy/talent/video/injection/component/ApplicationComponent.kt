package me.mfathy.talent.video.injection.component

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import me.mfathy.talent.video.QuestionsApp
import me.mfathy.talent.video.injection.module.*
import javax.inject.Singleton

/**
 * Dagger application components
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ViewModelsModule::class,
        DataModule::class,
        CacheModule::class,
        RemoteModule::class,
        UiModule::class
    ]
)

interface ApplicationComponent : AndroidInjector<QuestionsApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<QuestionsApp>()

}