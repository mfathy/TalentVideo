package me.mfathy.talent.video.data.mapper.remote

import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.store.remote.model.NetworkQuestion
import me.mfathy.talent.video.data.test.DataModelsFactory
import org.junit.Test

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for NetworkQuestionMapper
 */
class NetworkQuestionMapperTest{

    private val mapper = NetworkQuestionMapper()

    @Test
    fun testMapToEntityMapsData() {
        val domain = DataModelsFactory.makeNetworkQuestion()
        val entity = mapper.mapToEntity(domain)

        assertEqualData(entity, domain)
    }

    private fun assertEqualData(
        entity: QuestionEntity,
        domain: NetworkQuestion
    ) {
        kotlin.test.assertEquals(entity.qid, domain.qid)
        kotlin.test.assertEquals(entity.duration, domain.duration)
        kotlin.test.assertEquals(entity.question, domain.question)
    }
}