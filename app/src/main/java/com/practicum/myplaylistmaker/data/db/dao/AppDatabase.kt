package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.myplaylistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesDao(): TrackDao
}