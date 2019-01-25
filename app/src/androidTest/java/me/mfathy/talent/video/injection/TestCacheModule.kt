package me.mfathy.talent.video.injection

import android.app.Application
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import me.mfathy.talent.video.data.mapper.cache.CachedQuestionMapper
import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.store.cache.db.QuestionsDatabase

@Module
object TestCacheModule {

    @Provides
    @JvmStatic
    fun provideDatabase(application: Application): QuestionsDatabase {
        return QuestionsDatabase.getInstance(application)
    }

    @Provides
    @JvmStatic
    fun provideCacheStore(): QuestionsCacheStore {
        return mock()
    }

    @Provides
    @JvmStatic
    fun provideCacheMapper(): CachedQuestionMapper {
        return mock()
    }

}