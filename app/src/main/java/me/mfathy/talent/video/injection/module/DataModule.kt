package me.mfathy.talent.video.injection.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import me.mfathy.talent.video.data.QuestionsDataRepository
import me.mfathy.talent.video.data.mapper.data.QuestionEntityMapper
import me.mfathy.talent.video.domain.repository.InterviewRepository

/**
 * Dagger module to provide data repository dependencies.
 */
@Module
abstract class DataModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun providesQuestionEntityMapper(): QuestionEntityMapper = QuestionEntityMapper()
    }

    @Binds
    abstract fun bindDataRepository(dataRepository: QuestionsDataRepository): InterviewRepository
}