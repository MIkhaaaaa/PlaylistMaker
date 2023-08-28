package com.practicum.myplaylistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityMediaPlayer : AppCompatActivity() {
    companion object{
        lateinit var track: Track
    }
    private val back: ImageView by lazy { findViewById(R.id.back_menu_button) }
    private val pictureArtist: ImageView by lazy { findViewById(R.id.album_cover) }
    private val trackName: TextView by lazy { findViewById(R.id.playerTrackName) }
    private val bandName: TextView by lazy { findViewById(R.id.playerArtistName) }
    private val countdown: TextView by lazy { findViewById(R.id.trackTimer) }
    private val duration: TextView by lazy { findViewById(R.id.time) }
    private val album: TextView by lazy { findViewById(R.id.album) }
    private val year: TextView by lazy { findViewById(R.id.year) }
    private val genre: TextView by lazy { findViewById(R.id.genre) }
    private val country: TextView by lazy { findViewById(R.id.country) }
//    private val favourites: ImageView by lazy { findViewById(R.id.favourites) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        back.setOnClickListener {finish()}

        track = intent.getParcelableExtra("track")!!

        Glide.with(this)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.album)
            .centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(pictureArtist)

        trackName.text = track?.trackName ?: "Unknown track"
        bandName.text = track?.artistName ?: "Unknown artist"
        countdown.text = getString(R.string._0_30)
        duration.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
        album.text = track?.collectionName ?: "Unknown album"
        year.text = track?.releaseDate?.substring(0, 4) ?: "Unknown year"
        genre.text = track?.primaryGenreName ?: "Unknown genre"
        country.text = track?.country ?: "Unknown country"
    }
}