package com.practicum.myplaylistmaker.ui.favorites.fragmets

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    parentFragment: Fragment,
) :
    FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FavouritesFragment.newInstance()
        } else {
            PlaylistFragment.newInstance()
        }
    }

}