package com.practicum.myplaylistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentMediaPlayerBinding
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.player.PlayerViewModel
import com.practicum.myplaylistmaker.ui.player.adapters.PlaylistBottomSheetAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
    companion object {
        lateinit var track: Track
    }


    private var _bindingPlayer: FragmentMediaPlayerBinding ? = null
    private val bindingPlayer: FragmentMediaPlayerBinding
        get() = _bindingPlayer!!

    private lateinit var bottomNavigator: BottomNavigationView
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingPlayer = FragmentMediaPlayerBinding.inflate(layoutInflater)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.isVisible = false
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
            val fragmentmanager = requireActivity().supportFragmentManager
            bottomNavigator.visibility = VISIBLE
            fragmentmanager.popBackStack()
        }

        //BottomSheet
        val bottomSheetContainer = bindingPlayer.standardBottomSheet
        val overlay = bindingPlayer.overlay
        val bottomSheetBehavior = BottomSheetBehavior
            .from(bottomSheetContainer)
            .apply {
                state = STATE_HIDDEN
            }
        bottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            STATE_HIDDEN -> {
                                overlay.visibility = View.GONE
                            }

                            else -> {
                                overlay.visibility = VISIBLE
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )
        bindingPlayer.playlistAddButton.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
            bindingPlayer.overlay.visibility = VISIBLE
        }

        if (!viewModel.playlistList.value.isNullOrEmpty()) {
                playlistAdapter = viewModel.playlistList.value.let { it ->
                    PlaylistBottomSheetAdapter(it!!) {
                        playlistClickAdapting(track, it)
                        bottomSheetBehavior.state = STATE_HIDDEN
                    }
                }
        } else {
            playlistAdapter = PlaylistBottomSheetAdapter(emptyList()) {}
        }

        bindingPlayer.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        with(bindingPlayer.playlistRecycler){
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = playlistAdapter
        }

        viewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistList.isNullOrEmpty()) return@observe
            bindingPlayer.playlistRecycler.adapter = PlaylistBottomSheetAdapter(playlistList) {
                playlistClickAdapting(track, it)
                bottomSheetBehavior.state = STATE_HIDDEN
            }
        }
    }

    override fun onDestroy() {
        viewModel.jobCancel()
        _bindingPlayer = null
        super.onDestroy()
    }

    override fun onPause() {
        viewModel.pause()
        super.onPause()

    }

    fun playlistClickAdapting(track: Track, playlist: Playlist) {
        var trackIsAdded = false
        viewModel.addTrack(track, playlist)
        lifecycleScope.launch {
            delay(300)
            viewModel.playlistAdding.observe(viewLifecycleOwner) { playlistAdding ->
                val playlistName = playlist.playlistName
                if (!trackIsAdded) {
                    if (playlistAdding) {

                        val toastMessage = "Трек уже добавлен в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    } else {
                        val toastMessage = "Добавлено в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    }
                }
            }
        }
    }
}