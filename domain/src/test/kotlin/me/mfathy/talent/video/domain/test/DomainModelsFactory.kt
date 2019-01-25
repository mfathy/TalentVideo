package me.mfathy.talent.video.domain.test

import me.mfathy.talent.video.domain.model.Question
import me.mfathy.talent.video.domain.test.DomainDataFactory.randomBoolean
import me.mfathy.talent.video.domain.test.DomainDataFactory.randomInt
import me.mfathy.talent.video.domain.test.DomainDataFactory.randomString

/**
 * Created by Mohammed Fathy on 21/01/2019.
 * dev.mfathy@gmail.com
 */
object DomainModelsFactory {


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
}