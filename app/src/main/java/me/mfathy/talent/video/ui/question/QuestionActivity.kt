package me.mfathy.talent.video.ui.question

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import me.mfathy.talent.video.R

/**
 * App main activity.
 */
class QuestionActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity)
    }

}
