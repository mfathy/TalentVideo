package me.mfathy.talent.video.model

/**
 * Created by Mohammed Fathy on 23/01/2019.
 * dev.mfathy@gmail.com
 *
 * DataState sealed class to represent each type of data returned from domain layer.
 */
sealed class DataState {
    data class OnSuccess<T>(val data: T) : DataState()
    data class OnError(val error: Throwable) : DataState()
    object OnLoading : DataState()
}
