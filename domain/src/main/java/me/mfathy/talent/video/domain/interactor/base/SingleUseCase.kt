package me.mfathy.talent.video.domain.interactor.base

import io.reactivex.Single
import me.mfathy.talent.video.domain.executor.ExecutionThread

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * SingleUseCase is an abstract class which provide a Single observable to emit required data or error.
 */
abstract class SingleUseCase<T, in Params> constructor(
    private val postExecutionThread: ExecutionThread
) : UseCase<Single<T>, Params>() {

    protected abstract fun buildUseCaseSingle(params: Params? = null): Single<T>

    override fun execute(params: Params?): Single<T> {
        return this.buildUseCaseSingle(params)
            .subscribeOn(postExecutionThread.subscriber)
            .observeOn(postExecutionThread.observer)
    }
}