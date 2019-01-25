package me.mfathy.talent.video.data.store.cache.dao

import androidx.room.*
import io.reactivex.Single
import me.mfathy.talent.video.data.store.cache.model.CachedQuestion

/**
 * Created by Mohammed Fathy on 22/01/2019.
 * dev.mfathy@gmail.com
 *
 * Data access object for CachedQuestion
 */
@Dao
abstract class CachedQuestionDao {

    @Query("SELECT * FROM QUESTIONS WHERE QID = :qid")
    abstract fun getCachedQuestion(qid: Int): Single<CachedQuestion>

    @Query("SELECT COUNT(*) FROM QUESTIONS WHERE QID = :qid")
    abstract fun isQuestionCached(qid: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertQuestion(cachedQuestion: CachedQuestion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateQuestion(cachedQuestion: CachedQuestion)

    @Delete
    abstract fun deleteQuestion(cachedQuestion: CachedQuestion)
}