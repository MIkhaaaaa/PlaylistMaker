package com.practicum.myplaylistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ActivityMediaPlayer : AppCompatActivity() {
    companion object {
        lateinit var track: Track
        const val DELAY_PAUSE = 500L
    }

    private lateinit var bindingPlayer: ActivityMediaPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPlayer = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(bindingPlayer.root)

        bindingPlayer.backMenuButton.setOnClickListener {
            viewModel.playJob?.cancel()
            finish()
        }

        viewModel.stateLiveData.observe(this) {
            when (it) {
                PlayerState.STATE_PLAYING -> {
                    viewModel.pause()
                    it.apply { PlayerState.STATE_PAUSED }
                }

                PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                    viewModel.play()
                    it.apply { PlayerState.STATE_PLAYING }
                }

                else -> {
                    it.apply { PlayerState.STATE_DEFAULT }
                }
            }
        }

        @Suppress("DEPRECATION")
        track = intent.getParcelableExtra("track")!!

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.album)
            .centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(bindingPlayer.albumCover)

        with(bindingPlayer) {
            playerTrackName.text = track.trackName ?: "Unknown track"
            playerArtistName.text = track.artistName ?: "Unknown artist"
            trackTimer.text = getString(R.string._00_00)
            time.text =
                viewModel.formatMilliseconds(track.trackTimeMillis?.toLong() ?: 0)
            album.text = track.collectionName ?: "Unknown album"
            year.text = track.releaseDate?.substring(0, 4) ?: "Unknown year"
            genre.text = track.primaryGenreName ?: "Unknown genre"
            country.text = track.country ?: "Unknown country"
        }

        viewModel.createPlayer(track.previewUrl.toString())


        bindingPlayer.favourites.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }




        bindingPlayer.playButton.setOnClickListener {
            if (viewModel.clickDebounce()) {
                bindingPlayer.playButton.isVisible = false
                bindingPlayer.pauseButton.isVisible = true
                viewModel.play()
            }
        }

        bindingPlayer.pauseButton.setOnClickListener {
            bindingPlayer.playButton.isVisible = true
            bindingPlayer.pauseButton.isVisible = false
            viewModel.pause()
        }

        viewModel.getTimeLiveData().observe(this) { timer ->

            val statePlayer = viewModel.playerStateListener()
            if ((statePlayer == PlayerState.STATE_PLAYING) or (statePlayer == PlayerState.STATE_PAUSED)) {
                bindingPlayer.trackTimer.text = timer
            } else {
                bindingPlayer.trackTimer.text = timer
                bindingPlayer.playButton.isVisible = true
                bindingPlayer.pauseButton.isVisible = false

            }
        }

        viewModel.favouritesChecker(track).observe(this) { favourtitesIndicator ->
            if (favourtitesIndicator) {
                bindingPlayer.favourites.setBackgroundResource(R.drawable.button_like)
            } else bindingPlayer.favourites.setBackgroundResource(R.drawable.property)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        bindingPlayer.playButton.isVisible = true
        bindingPlayer.pauseButton.isVisible = false
    }

    override fun onDestroy() {
        viewModel.jobCancel()
        super.onDestroy()
    }


}