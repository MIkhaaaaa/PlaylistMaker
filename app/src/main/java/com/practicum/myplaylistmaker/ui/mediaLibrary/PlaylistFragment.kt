package com.practicum.myplaylistmaker.ui.mediaLibrary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.ui.favorites.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullablePlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = VISIBLE

        //кнопка создать плейлист
        nullablePlaylistBinding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.newPlaylistFragment)
        }

        nullablePlaylistBinding.playlistList.visibility = VISIBLE
        return nullablePlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        playlistAdapter = PlaylistAdapter {
            clickAdapting(it)
        }

        val recyclerView = nullablePlaylistBinding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter

        if (playlistViewModel.playlistList.value.isNullOrEmpty()) nullablePlaylistBinding.playlistList.visibility =
            GONE
        playlistViewModel.getPlaylists()

        playlistViewModel.playlistList.observe(viewLifecycleOwner) { it ->
            if (it.isNullOrEmpty()) {
                noPlaylist()
                return@observe
            } else {
                recyclerView.adapter = playlistAdapter
                playlistAdapter.setItems(it)
                existPlaylist()
                return@observe
            }
        }
    }

    private fun noPlaylist() {
        nullablePlaylistBinding.emptyPlaylist.visibility = VISIBLE
        nullablePlaylistBinding.emptyPlaylistText.visibility = VISIBLE
        nullablePlaylistBinding.playlistList.visibility = GONE
    }

    private fun existPlaylist() {
        nullablePlaylistBinding.emptyPlaylist.visibility = GONE
        nullablePlaylistBinding.emptyPlaylistText.visibility = GONE
        nullablePlaylistBinding.playlistList.visibility = VISIBLE
    }

    private fun clickAdapting(item: Playlist) {
        val navController = findNavController()
        navController.navigate(R.id.action_mediaLibraryFragment_to_playlistScreen, Bundle())
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}