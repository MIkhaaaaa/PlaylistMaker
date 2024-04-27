package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.myplaylistmaker.data.db.entity.TrackInPlaylistEntity

@Database(version = 1, entities = [TrackInPlaylistEntity::class])
abstract class TrackInPlaylistDataBase : RoomDatabase() {
    abstract fun trackListingDao(): TrackInPlaylistDAO
}