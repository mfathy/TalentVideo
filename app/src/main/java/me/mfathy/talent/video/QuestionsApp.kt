package me.mfathy.talent.video

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.mfathy.talent.video.injection.component.DaggerApplicationComponent

/**
 * Created by Mohammed Fathy on 22/01/2019.
 * dev.mfathy@gmail.com
 */
class QuestionsApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().create(this)
}