package me.mfathy.talent.video.data.store

import com.nhaarman.mockito_kotlin.mock
import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore
import me.mfathy.talent.video.data.store.cache.QuestionsCacheStoreImpl
import me.mfathy.talent.video.data.store.remote.QuestionsRemoteStoreImpl
import org.junit.Test

/**
 * Created by Mohammed Fathy on 26/01/2019.
 * dev.mfathy@gmail.com
 */
class DataStoreFactoryTest {

    private val mockCacheStore = mock<QuestionsCacheStoreImpl>()
    private val mockRemoteStore = mock<QuestionsRemoteStoreImpl>()
    private val factory = DataStoreFactory(
        mockCacheStore,
        mockRemoteStore
    )

    @Test
    fun testGetDataStoreReturnsRemoteSourceWhenNoCacheExists() {
        assert(factory.getDataStore(false) is QuestionsRemoteStoreImpl)
    }

    @Test
    fun testGetCacheDataStoreReturnsCacheDataStore() {
        assert(factory.getCacheDataStore() is QuestionsCacheStoreImpl)
    }

    @Test
    fun testGetRemoteDataStoreReturnsRemoteDataStore() {
        assert(factory.getRemoteDataStore() is QuestionsRemoteStoreImpl)
    }
}