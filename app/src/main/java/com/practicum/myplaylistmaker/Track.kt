
package com.practicum.myplaylistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Track(
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val trackId: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val track = other as Track
        return trackId == track.trackId
    }
    val artworkUrl512
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    override fun hashCode(): Int {
        return trackId.hashCode()
    }
}


