package com.practicum.myplaylistmaker.ui.favorites.fragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.myplaylistmaker.databinding.FragmentMediaBinding

class FragmentMediaFavorites :Fragment(), SelectPage {
    private var _binding: FragmentMediaBinding? = null
    private  val binding: FragmentMediaBinding
        get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(layoutInflater)
        binding.pager.adapter = FragmentAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding.pager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )
        return binding.root
    }
    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }

    override fun onDestroyView() {
        tabMediator.detach()
        super.onDestroyView()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}