package me.mfathy.talent.video.data.mapper.cache

import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.store.cache.model.CachedQuestion
import me.mfathy.talent.video.data.test.DataModelsFactory
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Mohammed Fathy on 24/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for CachedQuestionMapper
 */
class CachedQuestionMapperTest {

    private val mapper = CachedQuestionMapper()

    @Test
    fun testMapFromEntityMapsData() {
        val entity = DataModelsFactory.makeQuestionEntity()

        val domain = mapper.mapFromEntity(entity)

        assertEqualData(entity, domain)
    }

    @Test
    fun testMapToEntityMapsData() {
        val domain = DataModelsFactory.makeCachedQuestion()
        val entity = mapper.mapToEntity(domain)

        assertEqualData(entity, domain)
    }

    private fun assertEqualData(
        entity: QuestionEntity,
        domain: CachedQuestion
    ) {
        assertEquals(entity.qid, domain.qid)
        assertEquals(entity.answerVideoUrl, domain.answerVideoUrl)
        assertEquals(entity.duration, domain.duration)
        assertEquals(entity.isAnswered, domain.isAnswered)
        assertEquals(entity.question, domain.question)
    }
}