package me.mfathy.talent.video.data.store.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 */
@Entity(tableName = "questions")
data class CachedQuestion(
    @PrimaryKey var qid: Int = 0,
    @ColumnInfo(name = "question") val question: String = "",
    @ColumnInfo(name = "duration") val duration: Int = 0,
    @ColumnInfo(name = "is_answered") val isAnswered: Boolean = false,
    @ColumnInfo(name = "answer_video_url") val answerVideoUrl: String? = null
)
