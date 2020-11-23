package com.dhimas.dhiflix.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity

@Database(
    entities = [ShowEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShowDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ShowDatabase? = null

        fun getInstance(context: Context): ShowDatabase {
            if (INSTANCE == null) {
                synchronized(ShowDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ShowDatabase::class.java, "Shows.db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE as ShowDatabase
        }
    }

    abstract fun showDao(): ShowDao
}