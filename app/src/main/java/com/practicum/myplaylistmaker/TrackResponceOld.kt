package com.practicum.myplaylistmaker

import com.practicum.myplaylistmaker.domain.models.Track

data class TrackResponseOld ( val resultCount: Int,
                              val results: ArrayList<Track>)