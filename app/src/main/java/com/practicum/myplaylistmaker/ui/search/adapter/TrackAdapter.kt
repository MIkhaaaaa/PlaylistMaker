package com.practicum.myplaylistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.domain.models.Track

class TrackAdapter(private val clickListener: TrackClick,
                   private val longClickListener : LongClick


) : RecyclerView.Adapter<TrackViewHolder>(){
    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(_items[position])

        holder.itemView.setOnClickListener{
            clickListener.onClick(_items[position])
        }

        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick(_items[position])
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    fun setItems(items: List<Track>) {
        _items = items
        notifyDataSetChanged()
    }
    fun interface TrackClick {
        fun onClick(track: Track)
    }
    fun interface LongClick {
        fun onLongClick(track: Track)
    }
}