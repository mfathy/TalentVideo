package me.mfathy.talent.video.data.store.cache.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.mfathy.talent.video.data.store.cache.dao.CachedQuestionDao
import me.mfathy.talent.video.data.store.cache.model.CachedQuestion

/**
 * QuestionsDatabase: the room database initializer.
 */
@Database(entities = [CachedQuestion::class], version = 1, exportSchema = false)
abstract class QuestionsDatabase : RoomDatabase() {

    abstract fun cachedQuestions(): CachedQuestionDao

    companion object {

        private var INSTANCE: QuestionsDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): QuestionsDatabase {
            if (INSTANCE == null) {
                synchronized(lock) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            QuestionsDatabase::class.java, "questions_database"
                        ).build()
                    }
                    return INSTANCE as QuestionsDatabase
                }
            }
            return INSTANCE as QuestionsDatabase
        }
    }

}