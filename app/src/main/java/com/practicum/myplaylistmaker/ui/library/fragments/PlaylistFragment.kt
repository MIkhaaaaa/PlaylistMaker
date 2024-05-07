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
    private var _binding: FragmentPlaylistBinding? =  null
    private val binding:FragmentPlaylistBinding
        get() = _binding!!
    private lateinit var playlistAdapter: PlaylistAdapter

    private lateinit var bottomNavigator: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.isVisible = true

        binding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.newPlaylistFragment)
        }

        val recyclerView = binding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter= playlistViewModel.playlistList.value?.let { PlaylistAdapter {} }

        if (playlistViewModel.playlistList.value.isNullOrEmpty()) binding.playlistList.isVisible = false

        binding.playlistList.isVisible = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter {
            val bundle = Bundle()
            bundle.putParcelable("playlist", it)
            val navController = findNavController()
            navController.navigate(R.id.playlistScreen, bundle)
        }
        val recyclerView = binding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter

        if (playlistViewModel.playlistList.value.isNullOrEmpty()) binding.playlistList.isVisible = false

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

    private fun noPlaylist(){
        binding.emptyPlaylist.isVisible = true
        binding.emptyPlaylistText.isVisible = true
        binding.playlistList.isVisible = false
    }

    private fun existPlaylist(){
        binding.emptyPlaylist.isVisible = false
        binding.emptyPlaylistText.isVisible = false
        binding.playlistList.isVisible = true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}