package me.mfathy.talent.video.data.mapper.cache

import me.mfathy.talent.video.data.mapper.EntityMapper
import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.store.cache.model.CachedQuestion

/**
 * CachedQuestionMapper maps data to/from QuestionEntity, CachedQuestion
 */
class CachedQuestionMapper : EntityMapper<QuestionEntity, CachedQuestion> {
    override fun mapToEntity(domain: CachedQuestion): QuestionEntity = QuestionEntity(
        qid = domain.qid,
        question = domain.question,
        isAnswered = domain.isAnswered,
        duration = domain.duration,
        answerVideoUrl = domain.answerVideoUrl
    )

    override fun mapFromEntity(entity: QuestionEntity): CachedQuestion = CachedQuestion(
        qid = entity.qid,
        question = entity.question,
        isAnswered = entity.isAnswered,
        duration = entity.duration,
        answerVideoUrl = entity.answerVideoUrl
    )

}