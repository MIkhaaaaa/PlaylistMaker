package com.practicum.myplaylistmaker.ui.library.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentPlaylistScreenBinding
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.library.viewModels.PlaylistScreenViewModel
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreen : Fragment() {

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding: FragmentPlaylistScreenBinding
        get() = _binding!!


    private val playlistScreenViewModel by viewModel<PlaylistScreenViewModel>()
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)

        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.isVisible = false


        binding.playlistBackButtonArrow.setOnClickListener {
            onBackClick()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackClick()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlist = arguments?.getParcelable<Playlist>("playlist")

        playlist?.playlistId?.let { playlistScreenViewModel.getPlaylist(it) }

        val checkedPlaylist = playlist?.let { drawPlaylist(it) }
        if (checkedPlaylist != null) {
            drawCover(checkedPlaylist)
            drawMenuBottomSheet()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackListMaker() {
        playlistScreenViewModel.trackList.observe(viewLifecycleOwner) { trackList ->
            with(binding){
                if (trackList.isNullOrEmpty()) {
                    emptyList.isVisible = true
                    trackInPlaylistRecycler.isVisible = false
                } else {
                    trackAdapter.setItems(trackList)
                    trackAdapter.notifyDataSetChanged()
                    emptyList.isVisible = false
                    trackInPlaylistRecycler.isVisible = true
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTrackByClick(item: Track, playlist: Playlist) {
        playlistScreenViewModel.deleteTrack(item, playlist)
        playlistScreenViewModel.getTrackList(playlist)
        trackListMaker()
        drawPlaylist(playlist)
    }

    private fun deletePlaylist(item: Playlist) {
        playlistScreenViewModel.deletePlaylist(item)
        val navController = findNavController()
        navController.navigate(R.id.action_playlistScreen_to_mediaLibraryFragment2)
    }

    private fun suggestPlaylistDeleting(playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = playlistScreenViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deletePlaylist(playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun suggestTrackDeleting(track: Track, playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = playlistScreenViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Вы уверены, что хотите удалить трек из плейлиста??")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deleteTrackByClick(track, playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun sharePlaylist(playlist: Playlist) {
        val nameOfPlaylist = playlist.playlistName
        val desriptionOfPlaylist = playlist.description
        val trackNumber = playlist.arrayNumber
        if (trackNumber == 0) {
            val message = "В данном плейлисте нет списка треков, которым можно поделиться."
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            return
        }
        var trackInfo = "$nameOfPlaylist \n $desriptionOfPlaylist \n $trackNumber треков \n"
        val trackList: List<Track> = playlistScreenViewModel.trackList.value!!
        var i = 0
        trackList.forEach { track ->
            i += 1
            val name = track.trackName
            val duration = track.trackTimeMillis
            trackInfo = "$trackInfo $i. $name  - ($duration) \n"
        }

        val intentSend = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trackInfo)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        requireContext().startActivity(intentSend, null)
    }

    private fun onBackClick() {
        val fragmentmanager = requireActivity().supportFragmentManager
        bottomNavigator.isVisible = true
        fragmentmanager.popBackStack()
    }

    private fun playlistTime(playlist: Playlist) {
        playlistScreenViewModel.getPlaylistTime(playlist)
        playlistScreenViewModel.playlistTime.observe(viewLifecycleOwner) { playlistTime ->
            binding.playlistTime.text = playlistTime
        }
    }

    private fun drawPlaylist(playlist: Playlist): Playlist {
        var checkedPlaylist = playlist
        playlistScreenViewModel.updatedPlaylist.observe(viewLifecycleOwner) { updatedPlaylist ->
            checkedPlaylist = updatedPlaylist
            with(binding){
                PlaylistName.text = checkedPlaylist.playlistName
                menuPlaylistName.text = checkedPlaylist.playlistName
                descriptionOfPlaylist.text = checkedPlaylist.description ?: ""
                playlistTime(checkedPlaylist)

                //сколько треков в плейлисте
                val trackCounter = (checkedPlaylist.arrayNumber).toString()
                val text = when {
                    trackCounter.toInt() % 10 == 1 && trackCounter.toInt() % 100 != 11 -> " трек"
                    trackCounter.toInt() % 10 == 2 && trackCounter.toInt() % 100 != 12 -> " трека"
                    trackCounter.toInt() % 10 == 3 && trackCounter.toInt() % 100 != 13 -> " трека"
                    trackCounter.toInt() % 10 == 4 && trackCounter.toInt() % 100 != 14 -> " трека"
                    else -> " треков"
                }
                trackNumber.text = "$trackCounter $text"
                playlistNumber.text = "$trackCounter $text"
                drawCover(checkedPlaylist)
                drawPlaylistDataBottomSheet(checkedPlaylist)
            }

        }
        return checkedPlaylist
    }

    private fun drawCover(item: Playlist) {
        val getImage = item.uri
        if (getImage != "null") {
            binding.playlistPlaceHolder.isVisible = false
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())  //menuPlaylistImage
                .into(binding.playlistCover)

            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())  //menuPlaylistImage
                .into(binding.menuPlaylistImage)

        } else {
            binding.playlistPlaceHolder.isVisible = true
        }
    }

    private fun drawPlaylistDataBottomSheet(playlist: Playlist) {
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.trackInPlaylistContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        trackAdapter = TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    val bundle = Bundle()
                    bundle.putParcelable("track", it)
                    val navController = findNavController()
                    navController.navigate(R.id.action_playlistScreen_to_playerFragment, bundle)
                }
            },

            longClickListener = {
                suggestTrackDeleting(it, playlist)
            })


        playlistScreenViewModel.getTrackList(playlist)
        trackListMaker()


        playlistScreenViewModel.getTrackList(playlist)
        trackListMaker()

        with(binding){
            trackInPlaylistRecycler.layoutManager = LinearLayoutManager(requireContext())
            trackInPlaylistRecycler.adapter = trackAdapter

            share.setOnClickListener {
                sharePlaylist(playlist)
            }
            shareText.setOnClickListener {
                sharePlaylist(playlist)
            }
            editInfo.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("playlist", playlist)
                val navController = findNavController()
                navController.navigate(R.id.action_playlistScreen_to_editPlaylist, bundle)

            }
            deletePlaylistInfo.setOnClickListener {
                suggestPlaylistDeleting(playlist)
            }
        }
    }

    private fun drawMenuBottomSheet() {
        val menuBottomSheetContainer = binding.menuContainer
        val overlay = binding.overlay
        val menuBottomSheetBehavior = BottomSheetBehavior
            .from(menuBottomSheetContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        menuBottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                overlay.isVisible = false
                            }

                            else -> {
                                overlay.isVisible = true
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )

        binding.more.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}