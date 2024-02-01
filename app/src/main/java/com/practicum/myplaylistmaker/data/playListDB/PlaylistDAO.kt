package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface PlaylistDAO {

    @Insert(entity=PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist (playlist: PlaylistEntity)

    @Delete(entity=PlaylistEntity::class)
    fun deletePlaylist (playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun queryPlaylist () : List <PlaylistEntity>

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)
}