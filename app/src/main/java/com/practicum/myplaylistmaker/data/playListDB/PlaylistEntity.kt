package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistId:Int?,
    val playlistName:String,
    val description:String?,
    val uri:String,
    val trackList:List<Long?>,
    val arrayNumber:Int?
)