package com.nrh.musicopen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private val songs: List<Song>, private val listener: OnItemClickListener) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var filteredSongs: List<Song> = songs

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = filteredSongs[position]
        holder.bind(song)
        holder.itemView.setOnClickListener {
            listener.onItemClick(song)
        }
    }

    override fun getItemCount(): Int {
        return filteredSongs.size
    }

    fun filter(query: String) {
        filteredSongs = if (query.isEmpty()) {
            songs
        } else {
            songs.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.artist.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.song_title)
        private val artistTextView: TextView = itemView.findViewById(R.id.song_artist)

        fun bind(song: Song) {
            titleTextView.text = song.title
            artistTextView.text = song.artist
        }
    }
}
