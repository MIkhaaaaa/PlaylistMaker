package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.myplaylistmaker.data.db.entity.TrackInPlaylistEntity
import com.practicum.myplaylistmaker.domain.models.Track

@Dao
interface TrackInPlaylistDAO {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (track: Track)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long): TrackInPlaylistEntity?
}