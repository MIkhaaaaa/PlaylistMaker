package com.practicum.myplaylistmaker.domain.models

data class Playlist (
    val playlistId: Int,
    val playlistName:String,
    val description:String?,
    val uri:String,
    var trackArray:List<Long?>,
    var arrayNumber:Int?,
)