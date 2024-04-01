package com.practicum.myplaylistmaker.ui.player.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.PlaylistTrackerBinding
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistBottomSheetViewHolder(
    private val binding: PlaylistTrackerBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val text = when {
            innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11 -> " трек"
            innerNumber.toInt() % 10 == 2 && innerNumber.toInt() % 100 != 12 -> " трека"
            innerNumber.toInt() % 10 == 3 && innerNumber.toInt() % 100 != 13 -> " трека"
            innerNumber.toInt() % 10 == 4 && innerNumber.toInt() % 100 != 14 -> " трека"

            else -> " треков"
        }

        val number = "$innerNumber $text"
        binding.playlistNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)
        val width = 45
        val height = 45
        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.album)
            .transform(CenterCrop(), RoundedCorners(radius))
            .override(width, height)
            .into(binding.playlistImage)
    }
}