package me.mfathy.talent.video.data.model

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 */
data class QuestionEntity(
    val qid: Int,
    val question: String,
    val isAnswered: Boolean,
    val duration: Int,
    val answerVideoUrl: String?
)
