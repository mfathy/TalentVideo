package me.mfathy.talent.video.data.mapper.remote

import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.store.remote.model.NetworkQuestion

/**
 * QuestionEntityMapper maps data to/from QuestionEntity, NetworkQuestion
 */
open class NetworkQuestionMapper : NetworkEntityMapper<QuestionEntity, NetworkQuestion> {
    override fun mapToEntity(domain: NetworkQuestion): QuestionEntity = QuestionEntity(
        qid = domain.qid,
        question = domain.question,
        duration = domain.duration,
        isAnswered = false,
        answerVideoUrl = null
    )
}