package com.practicum.myplaylistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.util.Creator
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityMediaPlayer : AppCompatActivity() {
    companion object {
        lateinit var track: Track
        const val DELAY = 1000L
        const val DELAY_PAUSE = 500L
    }

    private var isClickAllowed = true
    private lateinit var mainThreadHandler: Handler
    private lateinit var bindingPlayer: ActivityMediaPlayerBinding
    private var url = ""
    private val creator = Creator.providePlayerInteractor()
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPlayer = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(bindingPlayer.root)
        mainThreadHandler = Handler(Looper.getMainLooper())
        bindingPlayer.backMenuButton.setOnClickListener { finish() }
        viewModel = ViewModelProvider(this,PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]

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

        viewModel.createPlayer(url,object : AudioPlayerInteractor.PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                when (state) {
                    PlayerState.STATE_PLAYING -> {
                        creator.pause()
                        state.apply { PlayerState.STATE_PAUSED }
                    }

                    PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                        creator.play()
                        state.apply { PlayerState.STATE_PLAYING }
                    }

                    else -> {
                        PlayerState.STATE_DEFAULT
                    }
                }
            }
        })



        bindingPlayer.playButton.setOnClickListener {
            if (clickDebounce()) {
                bindingPlayer.playButton.isVisible = false
                bindingPlayer.pauseButton.isVisible = true
                viewModel.play()
                mainThreadHandler.post(
                    createTimeLoop()
                )
            }
        }

        bindingPlayer.pauseButton.setOnClickListener {
            bindingPlayer.playButton.isVisible = true
            bindingPlayer.pauseButton.isVisible = false
            mainThreadHandler.removeCallbacksAndMessages(null)
            viewModel.pause()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        bindingPlayer.playButton.isVisible = true
        bindingPlayer.pauseButton.isVisible = false

    }

    override fun onResume() {
        super.onResume()
        bindingPlayer.trackTimer.text = timeTrack()
    }

    private fun createTimeLoop(): Runnable {
        return object : Runnable {
            override fun run() {
                val statePlayer = viewModel.playerStateListener()
                if ((statePlayer == PlayerState.STATE_PLAYING) or (statePlayer == PlayerState.STATE_PAUSED)) {
                    bindingPlayer.trackTimer.text = timeTrack()
                    mainThreadHandler.postDelayed(this, DELAY)
                } else {
                    bindingPlayer.trackTimer.text = getString(R.string._00_00)
                    bindingPlayer.playButton.isVisible = true
                    bindingPlayer.pauseButton.isVisible = false

                }
            }
        }
    }

    private fun timeTrack(): String {
        return viewModel.getTime()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler.postDelayed({ isClickAllowed = true }, DELAY_PAUSE)
        }
        return current
    }

}