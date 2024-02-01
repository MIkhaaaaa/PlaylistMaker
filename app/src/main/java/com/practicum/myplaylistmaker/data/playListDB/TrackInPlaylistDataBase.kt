package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackInPlaylistEntity::class])
abstract class TrackInPlaylistDataBase : RoomDatabase() {
    abstract fun trackListingDao(): TrackInPlaylistDAO
}