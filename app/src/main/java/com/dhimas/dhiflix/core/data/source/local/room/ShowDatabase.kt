package com.dhimas.dhiflix.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity

@Database(entities = [ShowEntity::class], version = 1, exportSchema = false)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
}