package com.practicum.myplaylistmaker.data.playlistsScreen

import com.practicum.myplaylistmaker.data.db.converters.TrackDbConvertor
import com.practicum.myplaylistmaker.data.db.dao.TrackInPlaylistDataBase
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistScreenRepositoryImpl(
    private val base: TrackInPlaylistDataBase
) : PlaylistScreenRepository {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        var trackList: List<Track> = emptyList()


        playlist.trackArray.map { id ->
            val entity = id?.let { base.trackListingDao().queryTrackId(searchId = it) } ?: return@map
            trackList = trackList + (TrackDbConvertor().mapTrackEntityToTrack(entity))
        }
        emit(trackList)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> = flow {
        var generalTime = 0
        var trackSeconds: Int

        playlist.trackArray.forEach {
            val entity = it?.let { it1 -> base.trackListingDao().queryTrackId(searchId = it1) }
            val track = (entity?.let { it1 -> TrackDbConvertor().mapTrackEntityToTrack(it1) })
            val time = track?.trackTimeMillis
//            trackSeconds =
//                (time?.split(":")?.get(0)?.toInt() ?: 0) * 60 + (time?.split(":")?.get(1)
//                    ?.toInt()
//                    ?: 0)
//            generalTime += trackSeconds
        }
//        val hours =  generalTime / (60 * 60)
//        val minutes = generalTime / 60
//        val seconds = generalTime % 60
//        val readyTime = if (hours == 0){
//            String.format("%02d:%02d", minutes, seconds)
//        } else {
//            String.format("%02d:%02d:%02d", hours, minutes, seconds)
//        }
        val readyTime = "02:20"
        emit(readyTime)
    }
}