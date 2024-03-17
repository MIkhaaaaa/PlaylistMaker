package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.myplaylistmaker.data.db.entity.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlayistDataBase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO
}