package me.mfathy.talent.video.data.store.remote

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Mohammed Fathy on 25/01/2019.
 * dev.mfathy@gmail.com
 *
 * Unit test for FakeRemoteService
 */
class FakeRemoteServiceTest {

    private val fakeRemoteService = FakeRemoteService()

    @Test
    fun testGetQuestionReturnCorrectQuestion() {
        val question = fakeRemoteService.getQuestion(0)
        assertEquals(question.qid, 0)
    }
}