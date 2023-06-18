package com.practicum.myplaylistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val albumsCover: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val bandName: TextView = itemView.findViewById(R.id.groupName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    var trackNumber:Long = 0

    fun bind(item: Track) {
        trackName.text = item.trackName
        bandName.text = item.artistName
        trackNumber = item.trackId
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.not_find)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(albumsCover)

        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
    }
}