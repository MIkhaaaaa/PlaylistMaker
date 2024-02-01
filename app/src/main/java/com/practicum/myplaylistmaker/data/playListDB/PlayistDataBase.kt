package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlayistDataBase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO
}