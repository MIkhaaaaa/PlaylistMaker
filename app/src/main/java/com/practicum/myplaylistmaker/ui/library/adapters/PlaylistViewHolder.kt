package com.practicum.myplaylistmaker.ui.library.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.PlaylistLayoutBinding
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistLittleName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val text = when {
            innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11 -> " трек"
            innerNumber.toInt() % 10 == 2 && innerNumber.toInt() % 100 != 12 -> " трека"
            innerNumber.toInt() % 10 == 3 && innerNumber.toInt() % 100 != 13 -> " трека"
            innerNumber.toInt() % 10 == 4 && innerNumber.toInt() % 100 != 14 -> " трека"
            else -> " треков"
        }
        val number = "$innerNumber $text"
        binding.playlistLittleSongNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)

        if (item.uri.isEmpty()) {
            val imageResource = R.drawable.album
            binding.playlistLittleCover.setImageResource(imageResource)
            val layoutParams = binding.playlistLittleCover.layoutParams
            layoutParams.width = R.dimen.VH_width
            layoutParams.height = R.dimen.VH_height
            binding.playlistLittleCover.layoutParams = layoutParams
        } else {

            val width = 160
            val height = 160
            Glide.with(itemView)
                .load(item.uri)
                .placeholder(R.drawable.album)
                .transform(CenterCrop(), RoundedCorners(radius))
                .override(width, height)
                .into(binding.playlistLittleCover)
        }
    }
}