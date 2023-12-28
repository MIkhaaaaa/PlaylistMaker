package com.practicum.myplaylistmaker.ui.player

import android.os.Bundle
import android.util.Log
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
import java.util.Locale

class ActivityMediaPlayer : AppCompatActivity() {
    companion object {
        lateinit var track: Track
        const val DELAY_PAUSE = 500L
    }

    private lateinit var bindingPlayer: ActivityMediaPlayerBinding
    private var url = ""
    private val viewModel: PlayerViewModel by viewModel()


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
                    it.apply { PlayerState.STATE_DEFAULT}
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

        Log.e("TRACK_TRACK", url)
        viewModel.createPlayer(url)




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