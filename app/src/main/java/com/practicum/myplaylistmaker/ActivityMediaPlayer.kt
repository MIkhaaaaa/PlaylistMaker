package com.practicum.myplaylistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityMediaPlayer : AppCompatActivity() {
    companion object {
        lateinit var track: Track
        const val DELAY = 1000L
        const val DELAY_PAUSE = 500L
        const val TRACK_TIME = 30000L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var isClickAllowed = true
    private var remainingTime = 0L
    private var trackTimeForScreen = 0L
    private var playerState = STATE_DEFAULT
    private lateinit var mainThreadHandler: Handler
    private lateinit var bindingPlayer: ActivityMediaPlayerBinding
    private val mediaPlayer = MediaPlayer()
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPlayer = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(bindingPlayer.root)
        mainThreadHandler = Handler(Looper.getMainLooper())
        bindingPlayer.backMenuButton.setOnClickListener { finish() }
        bindingPlayer.playButton.isEnabled = false

        track = intent.getParcelableExtra("track")!!

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.album)
            .centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(bindingPlayer.albumCover!!)

        bindingPlayer.playerTrackName.text = track.trackName ?: "Unknown track"
        bindingPlayer.playerArtistName.text = track.artistName ?: "Unknown artist"
        bindingPlayer.trackTimer.text = getString(R.string._00_00)
        bindingPlayer.time.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        bindingPlayer.album.text = track.collectionName ?: "Unknown album"
        bindingPlayer.year.text = track.releaseDate?.substring(0, 4) ?: "Unknown year"
        bindingPlayer.genre.text = track.primaryGenreName ?: "Unknown genre"
        bindingPlayer.country.text = track.country ?: "Unknown country"
        url = track.previewUrl.toString()

        preparePlayer()

        bindingPlayer.playButton.setOnClickListener {
            if (clickDebounce()) {
                val time = System.currentTimeMillis()
                trackTimeForScreen = if (remainingTime != 0L) {
                    remainingTime
                } else {
                    TRACK_TIME
                }
                mainThreadHandler.post(
                    createTimeLoop(time, trackTimeForScreen)
                )
            }
            playbackControl()


        }
        bindingPlayer.pauseButton.setOnClickListener {
                mainThreadHandler.removeCallbacksAndMessages(null)
                playbackControl()


        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            bindingPlayer.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            bindingPlayer.playButton.visibility = View.VISIBLE
            bindingPlayer.pauseButton.visibility = View.GONE
            playerState = STATE_PREPARED

        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        bindingPlayer.playButton.visibility = View.GONE
        bindingPlayer.pauseButton.visibility = View.VISIBLE
        playerState = STATE_PLAYING

    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        bindingPlayer.playButton.visibility = View.VISIBLE
        bindingPlayer.pauseButton.visibility = View.GONE
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    private fun createTimeLoop(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                 remainingTime = duration - elapsedTime
                if (remainingTime > 0) {
                    val sec = remainingTime / DELAY
                    bindingPlayer.trackTimer.text = String.format("%d:%02d", sec / 60, sec % 60)
                    mainThreadHandler.postDelayed(this, DELAY)
                } else {
                    bindingPlayer.trackTimer.text = getString(R.string._00_00)
                    remainingTime = 0L
                    bindingPlayer.playButton.visibility = View.VISIBLE
                    bindingPlayer.pauseButton.visibility = View.GONE

                }
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler.postDelayed({isClickAllowed = true}, DELAY_PAUSE)
        }
        return current
    }

}