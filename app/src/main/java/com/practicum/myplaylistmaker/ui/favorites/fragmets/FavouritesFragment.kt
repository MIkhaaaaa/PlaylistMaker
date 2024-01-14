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
    private lateinit var nullableFavouritesBinding: FragmentFavoritesBinding
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
        nullableFavouritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        nullableFavouritesBinding.favouritesRecycler.layoutManager = LinearLayoutManager(requireContext())
        nullableFavouritesBinding.favouritesRecycler.adapter = favouritesAdapter
        return nullableFavouritesBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.favouritesMaker().observe(viewLifecycleOwner) { trackResultList ->
            if (favouritesViewModel.trackResultList.value.isNullOrEmpty()) {
                nullableFavouritesBinding.emptyMediaLibrary.isVisible = true
                 nullableFavouritesBinding.emptyMediaLibraryText.isVisible = true
                nullableFavouritesBinding.favouritesRecycler.isVisible = false
                favouritesAdapter.notifyDataSetChanged()
            } else {
                nullableFavouritesBinding.emptyMediaLibrary.visibility = View.GONE
                nullableFavouritesBinding.emptyMediaLibrary.isVisible = false
                nullableFavouritesBinding.emptyMediaLibraryText.isVisible = false
                nullableFavouritesBinding.favouritesRecycler.isVisible = true
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