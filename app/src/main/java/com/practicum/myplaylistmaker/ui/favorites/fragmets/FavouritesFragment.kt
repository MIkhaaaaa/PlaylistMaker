package com.practicum.myplaylistmaker.ui.favorites.fragmets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.favorites.viewModel.FavouritesViewModel
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    private val favouritesViewModel by viewModel<FavouritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding
    private var isClickAllowed = true
    private val favouritesAdapter: TrackAdapter by lazy {
        TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            tracks = emptyList()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.rvFavouritesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavouritesRecycler.adapter = favouritesAdapter
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.favouritesMaker().observe(viewLifecycleOwner) { trackResultList ->
            if (favouritesViewModel.trackResultList.value.isNullOrEmpty()) {
                binding.tvEmptyMediaLibraryText.isVisible = true
                 binding.tvEmptyMediaLibraryText.isVisible = true
                binding.rvFavouritesRecycler.isVisible = false
                favouritesAdapter.notifyDataSetChanged()
            } else {
                binding.ivEmptyMediaLibrary.visibility = View.GONE
                binding.ivEmptyMediaLibrary.isVisible = false
                binding.tvEmptyMediaLibraryText.isVisible = false
                binding.rvFavouritesRecycler.isVisible = true
                favouritesAdapter.setItems(favouritesViewModel.trackResultList.value!!)
                favouritesAdapter.notifyDataSetChanged()
            }
        }



    }

    private fun clickAdapting(item: Track) {
        favouritesViewModel.addItem(item)
        val intent = Intent(requireContext(), ActivityMediaPlayer::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
    }

    companion object {
        fun newInstance() = FavouritesFragment()

    }
}