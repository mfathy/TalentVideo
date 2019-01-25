package me.mfathy.talent.video.ui.question

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Mohammed Fathy on 23/01/2019.
 * dev.mfathy@gmail.com
 *
 * BaseViewModel to manage disposable for all sub view models.
 */
open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    /**
     * Extension function to add Rx disposables to be easy for clean up.
     */
    open fun Disposable.add() {
        disposables.add(this)
    }

    open fun clearDisposables() {
        if (!disposables.isDisposed) disposables.dispose()
    }

}