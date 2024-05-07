package com.practicum.myplaylistmaker.ui.favorites.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.favorites.viewModel.FavouritesViewModel
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    private val favouritesViewModel by viewModel<FavouritesViewModel>()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!


    private var isClickAllowed = true
    private val favouritesAdapter: TrackAdapter by lazy {
        TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = {

            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        with(binding) {
            rvFavouritesRecycler.layoutManager = LinearLayoutManager(requireContext())
            rvFavouritesRecycler.adapter = favouritesAdapter
            ivEmptyMediaLibrary.visibility = View.GONE
            ivEmptyMediaLibrary.isVisible = false
            tvEmptyMediaLibraryText.isVisible = false
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.favouritesMaker().observe(viewLifecycleOwner) { trackResultList ->
            with(binding) {
                if (favouritesViewModel.trackResultList.value.isNullOrEmpty()) {
                    tvEmptyMediaLibraryText.isVisible = true
                    tvEmptyMediaLibraryText.isVisible = true
                    rvFavouritesRecycler.isVisible = false
                    favouritesAdapter.notifyDataSetChanged()
                } else {
                    ivEmptyMediaLibrary.visibility = View.GONE
                    ivEmptyMediaLibrary.isVisible = false
                    tvEmptyMediaLibraryText.isVisible = false
                    rvFavouritesRecycler.isVisible = true
                    favouritesAdapter.setItems(favouritesViewModel.trackResultList.value!!)
                    favouritesAdapter.notifyDataSetChanged()
                }
            }

        }

    }

    private fun clickAdapting(item: Track) {
        favouritesViewModel.addItem(item)
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        val navController = findNavController()
        navController.navigate(R.id.playerFragment, bundle)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = FavouritesFragment()

    }
}