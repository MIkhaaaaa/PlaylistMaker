package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.myplaylistmaker.domain.models.Track

@Dao
interface TrackInPlaylistDAO {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: Track)


}