package me.mfathy.talent.video.data.store.remote

import me.mfathy.talent.video.data.store.remote.model.NetworkQuestion

open class FakeRemoteService {

    private val questions = listOf(
        NetworkQuestion(
            qid = 0,
            question = "Define your strengths?",
            duration = 45
        ),
        NetworkQuestion(
            qid = 1,
            question = "Define your weaknesses?",
            duration = 45
        ),
        NetworkQuestion(
            qid = 2,
            question = "Introduce yourself?",
            duration = 45
        ),
        NetworkQuestion(
            qid = 3,
            question = "What makes the best team for you?",
            duration = 45
        )
    )

    fun getQuestion(qid: Int): NetworkQuestion {
        return questions.first { it.qid == qid }
    }
}
