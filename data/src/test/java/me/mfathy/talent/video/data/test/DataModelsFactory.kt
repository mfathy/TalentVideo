package me.mfathy.talent.video.data.test

import me.mfathy.talent.video.data.model.QuestionEntity
import me.mfathy.talent.video.data.store.cache.model.CachedQuestion
import me.mfathy.talent.video.data.store.remote.model.NetworkQuestion
import me.mfathy.talent.video.data.test.DataFactory.randomBoolean
import me.mfathy.talent.video.data.test.DataFactory.randomInt
import me.mfathy.talent.video.data.test.DataFactory.randomString
import me.mfathy.talent.video.domain.model.Question

/**
 * Created by Mohammed Fathy on 21/01/2019.
 * dev.mfathy@gmail.com
 */
object DataModelsFactory {


    fun makeQuestionList(count: Int): List<Question> {
        val mutableList = mutableListOf<Question>()
        repeat(count) {
            mutableList.add(makeQuestion())
        }
        return mutableList.toList()
    }

    fun makeQuestion(): Question = Question(
        randomInt(),
        randomString(),
        randomBoolean(),
        randomInt(),
        randomString()
    )

    fun makeQuestionEntity(): QuestionEntity = QuestionEntity(
        randomInt(),
        randomString(),
        randomBoolean(),
        randomInt(),
        randomString()
    )

    fun makeCachedQuestion(): CachedQuestion = CachedQuestion(
        qid = randomInt(),
        question = randomString(),
        duration = randomInt(),
        isAnswered = randomBoolean(),
        answerVideoUrl = randomString()
    )

    fun makeNetworkQuestion(): NetworkQuestion = NetworkQuestion(
        qid = randomInt(),
        question = randomString(),
        duration = randomInt()
    )
}