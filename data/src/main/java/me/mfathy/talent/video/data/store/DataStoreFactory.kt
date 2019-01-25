package me.mfathy.talent.video.data.store

import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.repository.QuestionsDataStore
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore
import me.mfathy.talent.video.data.store.cache.QuestionsCacheStoreImpl
import me.mfathy.talent.video.data.store.remote.QuestionsRemoteStoreImpl
import javax.inject.Inject

open class DataStoreFactory @Inject constructor(
    private val cacheDataStore: QuestionsCacheStoreImpl,
    private val remoteDataStore: QuestionsRemoteStoreImpl
) {
    open fun getDataStore(isCached: Boolean): QuestionsDataStore {
        return if (isCached) {
            cacheDataStore
        } else {
            remoteDataStore
        }
    }

    open fun getCacheDataStore(): QuestionsCacheStore {
        return cacheDataStore
    }

    open fun getRemoteDataStore(): QuestionsRemoteStore {
        return remoteDataStore
    }
}
