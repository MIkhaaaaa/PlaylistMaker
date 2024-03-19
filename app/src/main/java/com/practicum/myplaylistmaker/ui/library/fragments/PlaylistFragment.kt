package com.practicum.myplaylistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.practicum.myplaylistmaker.ui.favorites.viewModel.PlaylistViewModel
import com.practicum.myplaylistmaker.ui.library.adapters.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullablePlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.isVisible = true

        //кнопка создать плейлист
        nullablePlaylistBinding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.newPlaylistFragment)
        }

        //список плейлистов
        val recyclerView = nullablePlaylistBinding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter= playlistViewModel.playlistList.value?.let { PlaylistAdapter(it, {}) }
        if (playlistViewModel.playlistList.value.isNullOrEmpty()) nullablePlaylistBinding.playlistList.isVisible = false

        nullablePlaylistBinding.playlistList.isVisible = true
        return nullablePlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistViewModel.playlistMaker().value.isNullOrEmpty()) {
                noPlaylist()
                return@observe
            } else {
                nullablePlaylistBinding.playlistList.adapter= PlaylistAdapter(playlistList) {}
                existPlaylist()
                return@observe
            }
        }
    }

    private fun noPlaylist(){
        nullablePlaylistBinding.emptyPlaylist.isVisible = true
        nullablePlaylistBinding.emptyPlaylistText.isVisible = true
        nullablePlaylistBinding.playlistList.isVisible = false
    }

    private fun existPlaylist(){
        nullablePlaylistBinding.emptyPlaylist.isVisible = false
        nullablePlaylistBinding.emptyPlaylistText.isVisible = false
        nullablePlaylistBinding.playlistList.isVisible = true
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}