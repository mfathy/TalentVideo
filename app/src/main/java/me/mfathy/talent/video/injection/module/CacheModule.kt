package me.mfathy.talent.video.injection.module

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import me.mfathy.talent.video.data.mapper.cache.CachedQuestionMapper
import me.mfathy.talent.video.data.repository.QuestionsCacheStore
import me.mfathy.talent.video.data.store.cache.QuestionsCacheStoreImpl
import me.mfathy.talent.video.data.store.cache.db.QuestionsDatabase

/**
 * Created by Mohammed Fathy on 08/12/2018.
 * dev.mfathy@gmail.com
 *
 * Dagger module to provide cache dependencies.
 */
@Module
abstract class CacheModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun providesDatabase(application: Application): QuestionsDatabase {
            return QuestionsDatabase.getInstance(application)
        }

        @Provides
        @JvmStatic
        fun providesCachedQuestionMapper(): CachedQuestionMapper = CachedQuestionMapper()
    }


    @Binds
    abstract fun bindCacheStore(cache: QuestionsCacheStoreImpl): QuestionsCacheStore
}