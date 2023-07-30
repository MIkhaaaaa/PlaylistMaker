package com.practicum.myplaylistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val tracks: List<Track>
,  private val clickListener:TrackClick) : RecyclerView.Adapter<TrackViewHolder>(){
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
    fun interface TrackClick {
        fun onClick(track:Track)
    }
}