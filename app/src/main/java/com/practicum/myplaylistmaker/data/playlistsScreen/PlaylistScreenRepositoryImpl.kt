package com.practicum.myplaylistmaker.data.playlistsScreen

import com.practicum.myplaylistmaker.data.db.converters.TrackDbConvertor
import com.practicum.myplaylistmaker.data.db.dao.TrackInPlaylistDataBase
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration

class PlaylistScreenRepositoryImpl(
    private val base: TrackInPlaylistDataBase
) : PlaylistScreenRepository {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        var trackList: List<Track> = emptyList()


        playlist.trackArray.map { id ->
            val entity =
                id?.let { base.trackListingDao().queryTrackId(searchId = it) } ?: return@map
            trackList = trackList + (TrackDbConvertor().mapTrackEntityToTrack(entity))
        }
        emit(trackList)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> = flow {
        var generalTime: Long = 0

        playlist.trackArray.forEach {
            val entity = it?.let { it1 -> base.trackListingDao().queryTrackId(searchId = it1) }
            val track = (entity?.let { it1 -> TrackDbConvertor().mapTrackEntityToTrack(it1) })
            val timeMillis = track?.trackTimeMillis?.toLong() ?: 0

            generalTime += timeMillis
        }

        val trackTimes = formatMilliseconds(generalTime)
        emit(trackTimes)
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val duration = Duration.ofMillis(milliseconds)

        //val hours = duration.toHours()
        //val seconds = duration.minusMinutes(minutes).seconds
        val minutes = duration.toMinutes()

        val text = when {
            minutes.toInt() % 10 == 1 && minutes.toInt() % 100 != 11 -> " минута"
            minutes.toInt() % 10 == 2 && minutes.toInt() % 100 != 12 -> " минуты"
            minutes.toInt() % 10 == 3 && minutes.toInt() % 100 != 13 -> " минут"
            else -> " минут"
        }

        return "${minutes.toInt()}$text "

    }

}