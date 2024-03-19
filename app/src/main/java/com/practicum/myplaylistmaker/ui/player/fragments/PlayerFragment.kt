package com.practicum.myplaylistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
    companion object {
        lateinit var track: Track
    }


    private var _bindingPlayer: ActivityMediaPlayerBinding? = null
    private val bindingPlayer: ActivityMediaPlayerBinding
        get() = _bindingPlayer!!



    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingPlayer = ActivityMediaPlayerBinding.inflate(layoutInflater)
        return bindingPlayer.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
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
        track = arguments?.getParcelable("track")!!

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

        viewModel.getTimeLiveData().observe(viewLifecycleOwner) { timer ->

            val statePlayer = viewModel.playerStateListener()
            if ((statePlayer == PlayerState.STATE_PLAYING) or (statePlayer == PlayerState.STATE_PAUSED)) {
                bindingPlayer.trackTimer.text = timer
            } else {
                bindingPlayer.trackTimer.text = timer
                bindingPlayer.playButton.isVisible = true
                bindingPlayer.pauseButton.isVisible = false

            }
        }

        viewModel.favouritesChecker(track).observe(viewLifecycleOwner) { favourtitesIndicator ->
            if (favourtitesIndicator) {
                bindingPlayer.favourites.setBackgroundResource(R.drawable.button_like)
            } else bindingPlayer.favourites.setBackgroundResource(R.drawable.property)
        }

        bindingPlayer.backMenuButton.setOnClickListener {
            viewModel.playJob?.cancel()
        }

    }

    override fun onDestroy() {
        viewModel.jobCancel()
        _bindingPlayer = null
        super.onDestroy()
    }










}