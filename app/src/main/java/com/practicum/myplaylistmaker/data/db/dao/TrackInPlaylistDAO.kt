package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.myplaylistmaker.data.db.entity.PlaylistEntity
import com.practicum.myplaylistmaker.data.db.entity.TrackEntity
import com.practicum.myplaylistmaker.data.db.entity.TrackInPlaylistEntity
import com.practicum.myplaylistmaker.domain.models.Track

@Dao
interface TrackInPlaylistDAO {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (track: Track)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long): TrackEntity?

    @Delete(entity= PlaylistEntity::class)
    fun deletePlaylist (playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId=:searchId")
    fun findPlaylist (searchId:Int) : PlaylistEntity

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)
}