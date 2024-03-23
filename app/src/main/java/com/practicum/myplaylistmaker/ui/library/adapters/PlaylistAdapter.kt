package com.practicum.myplaylistmaker.ui.library.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.myplaylistmaker.databinding.PlaylistLayoutBinding
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistAdapter(
    private var playlist: List<Playlist>,
    private val clickListener: PlaylistClick
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlist[position])
            //notifyDataSetChanged()
        }
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }
    fun interface playlistClickAdapting {
        fun onClick(playlist: Playlist)
    }

}