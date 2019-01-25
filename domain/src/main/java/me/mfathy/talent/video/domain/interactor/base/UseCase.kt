package me.mfathy.talent.video.domain.interactor.base

/**
 * Created by Mohammed Fathy on 22/01/2019.
 * dev.mfathy@gmail.com
 *
 * A Base use case for domain layer interactors.
 */
abstract class UseCase<T, in Params> {
    abstract fun execute(params: Params?): T
}