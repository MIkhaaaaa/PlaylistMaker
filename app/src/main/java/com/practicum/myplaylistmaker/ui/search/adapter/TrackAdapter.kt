package com.practicum.myplaylistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.domain.models.Track

class TrackAdapter(var tracks: List<Track>
                   , private val clickListener: TrackClick
) : RecyclerView.Adapter<TrackViewHolder>(){
    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener{
            clickListener.onClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setItems(items: List<Track>) {
        tracks = items
        notifyDataSetChanged()
    }
    fun interface TrackClick {
        fun onClick(track: Track)
    }
}