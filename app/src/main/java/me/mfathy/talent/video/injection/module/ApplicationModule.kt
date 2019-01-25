package me.mfathy.talent.video.injection.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import me.mfathy.talent.video.QuestionsApp


/**
 * Dagger application module to provide app context.
 */
@Module
abstract class ApplicationModule {

    @Binds
    abstract fun bindContext(application: QuestionsApp): Context

    @Binds
    abstract fun bindApplication(application: QuestionsApp): Application

}
