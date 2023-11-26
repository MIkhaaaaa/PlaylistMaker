package com.practicum.myplaylistmaker.ui.favorites.fragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.practicum.myplaylistmaker.ui.favorites.viewModel.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    private val favouritesViewModel by viewModel<FavouritesViewModel>()
    private lateinit var nullableFavouritesBinding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return nullableFavouritesBinding.root
    }

    companion object {
        fun newInstance() = FavouritesFragment()

    }
}