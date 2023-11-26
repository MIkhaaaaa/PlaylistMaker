package com.practicum.myplaylistmaker.ui.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.myplaylistmaker.databinding.ActivivtyMediaBinding
import com.practicum.myplaylistmaker.ui.favorites.fragmets.FragmentAdapter
import com.practicum.myplaylistmaker.ui.favorites.fragmets.SelectPage

class ActivityMediaFavorites : AppCompatActivity(), SelectPage {
    private lateinit var binding: ActivivtyMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivivtyMediaBinding.inflate(layoutInflater)
        binding.backButtonArrow.setOnClickListener {
            finish()
        }
        setContentView(binding.root)

        val adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter

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

    }


    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}