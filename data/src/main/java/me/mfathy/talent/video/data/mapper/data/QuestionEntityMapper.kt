package me.mfathy.talent.video.data.mapper.data

import me.mfathy.talent.video.data.mapper.EntityMapper
import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.domain.model.Question

/**
 * QuestionEntityMapper maps data to/from QuestionEntity, Question
 */
open class QuestionEntityMapper : EntityMapper<QuestionEntity, Question> {
    override fun mapToEntity(domain: Question): QuestionEntity = QuestionEntity(
        qid = domain.qid,
        question = domain.question,
        isAnswered = domain.isAnswered,
        duration = domain.duration,
        answerVideoUrl = domain.answerVideoUrl
    )

    override fun mapFromEntity(entity: QuestionEntity): Question = Question(
        qid = entity.qid,
        question = entity.question,
        isAnswered = entity.isAnswered,
        duration = entity.duration,
        answerVideoUrl = entity.answerVideoUrl
    )

}