package me.mfathy.talent.video.data.mapper.data

import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.test.DataModelsFactory
import me.mfathy.talent.video.domain.model.Question
import org.junit.Test

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for QuestionEntityMapper
 */
class QuestionEntityMapperTest {

    private val mapper = QuestionEntityMapper()

    @Test
    fun testMapFromEntityMapsData() {
        val entity = DataModelsFactory.makeQuestionEntity()

        val domain = mapper.mapFromEntity(entity)

        assertEqualData(entity, domain)
    }

    @Test
    fun testMapToEntityMapsData() {
        val domain = DataModelsFactory.makeQuestion()
        val entity = mapper.mapToEntity(domain)

        assertEqualData(entity, domain)
    }

    private fun assertEqualData(
        entity: QuestionEntity,
        domain: Question
    ) {
        kotlin.test.assertEquals(entity.qid, domain.qid)
        kotlin.test.assertEquals(entity.answerVideoUrl, domain.answerVideoUrl)
        kotlin.test.assertEquals(entity.duration, domain.duration)
        kotlin.test.assertEquals(entity.isAnswered, domain.isAnswered)
        kotlin.test.assertEquals(entity.question, domain.question)
    }
}