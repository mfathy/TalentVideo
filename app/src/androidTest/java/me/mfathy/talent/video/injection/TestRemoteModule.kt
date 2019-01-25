package me.mfathy.talent.video.injection

import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore

@Module
object TestRemoteModule {

    @Provides
    @JvmStatic
    fun provideRemoteDataStore(): QuestionsRemoteStore {
        return mock()
    }

}