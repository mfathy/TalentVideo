package me.mfathy.talent.video.domain.interactor.base

import io.reactivex.Completable
import me.mfathy.talent.video.domain.executor.ExecutionThread

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * A CompletableUseCase is an abstract class which provide a Completable observable which completes or return error.
 */
abstract class CompletableUseCase<in Params> constructor(
    private val postExecutionThread: ExecutionThread
) : UseCase<Completable, Params>() {

    protected abstract fun buildUseCaseCompletable(params: Params? = null): Completable

    override fun execute(params: Params?): Completable {
        return this.buildUseCaseCompletable(params)
            .subscribeOn(postExecutionThread.subscriber)
            .observeOn(postExecutionThread.observer)
    }
}