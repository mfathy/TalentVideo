package me.mfathy.talent.video.injection.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import me.mfathy.talent.video.data.mapper.remote.NetworkQuestionMapper
import me.mfathy.talent.video.data.repository.QuestionsRemoteStore
import me.mfathy.talent.video.data.store.remote.FakeRemoteService
import me.mfathy.talent.video.data.store.remote.QuestionsRemoteStoreImpl

/**
 * Created by Mohammed Fathy on 08/12/2018.
 * dev.mfathy@gmail.com
 *
 * Dagger module to provide remote dependencies.
 */
@Module
abstract class RemoteModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun providesNetworkQuestionMapper(): NetworkQuestionMapper = NetworkQuestionMapper()

        @Provides
        @JvmStatic
        fun providesFakeRemoteService(): FakeRemoteService = FakeRemoteService()
    }

    @Binds
    abstract fun bindRemoteStore(remote: QuestionsRemoteStoreImpl): QuestionsRemoteStore

}