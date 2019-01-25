package me.mfathy.talent.video.domain.model

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * Question is the domain model for 1 question of an interview process.
 */
data class Question(
    val qid: Int,
    val question: String,
    var isAnswered: Boolean,
    val duration: Int,
    var answerVideoUrl: String?
)
