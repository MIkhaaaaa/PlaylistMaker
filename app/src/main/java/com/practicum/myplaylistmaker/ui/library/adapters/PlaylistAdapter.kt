package com.practicum.myplaylistmaker.ui.library.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.myplaylistmaker.databinding.PlaylistLayoutBinding
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistAdapter(
    private val clickListener: PlaylistClick

) :RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlists[position])
            notifyDataSetChanged()
        }
    }

    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

}