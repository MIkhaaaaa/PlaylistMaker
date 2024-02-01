package com.practicum.myplaylistmaker.data.playListDB

class PlaylistRepositoryImpl(
    private val playistDataBase: PlayistDataBase,
    private val converter: PlaylistConverter,
    private val trackInDataBase: TrackInPlaylistDataBase
) : PlaylistRepository {

    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val playlist = Playlist(
            null,
            playlistName,
            description,
            uri,
            emptyList(),
            0
        )
        playistDataBase.playlistDao().insertPlaylist(
            converter.mapplaylistClassToEntity(playlist)
        )
    }

    override fun deletePlaylist(item: Playlist) {
        converter.mapplaylistClassToEntity(item)
            ?.let { playistDataBase.playlistDao().deletePlaylist(it) }
    }

    override fun queryPlaylist(): Flow<List<Playlist>> = flow {
        val playlistConverted =
            playistDataBase.playlistDao().queryPlaylist()
                .map { converter.mapplaylistEntityToClass(it) }
        emit(playlistConverted)
        return@flow
    }

    override fun update(track: Track, playlist: Playlist) {
        playistDataBase.playlistDao().updatePlaylist(converter.mapplaylistClassToEntity(playlist))
        trackInDataBase.trackListingDao().insertTrack(track)
    }

    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val newPlaylist = Playlist(
            playlist.playlistId,
            playlistName,
            description,
            uri,
            playlist.trackArray,
            playlist.arrayNumber
        )
        playistDataBase.playlistDao().updatePlaylist(
            converter.mapplaylistClassToEntity(newPlaylist)
        )
    }

    override fun findPlaylist(searchId: Int): Flow<Playlist> = flow {
        val playlistConverted = playistDataBase.playlistDao().findPlaylist(searchId)
        val playlist = converter.mapplaylistEntityToClass(playlistConverted)
        emit(playlist)
        return@flow
    }
}