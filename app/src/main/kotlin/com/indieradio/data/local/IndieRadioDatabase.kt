package com.indieradio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.indieradio.data.local.dao.FavoriteDao
import com.indieradio.data.local.dao.HistoryDao
import com.indieradio.data.local.entity.FavoriteEntity
import com.indieradio.data.local.entity.HistoryEntity

/**
 * Main Room database for IndieRadio
 * Version 1: Initial database with favorites and history
 */
@Database(
    entities = [
        FavoriteEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class IndieRadioDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao

    companion object {
        const val DATABASE_NAME = "indieradio_db"
    }
}
