package com.nrh.musicopen

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NowPlayingActivity : AppCompatActivity() {

    private lateinit var tvSongTitle: TextView
    private lateinit var tvSongArtist: TextView
    private lateinit var ivAlbumArt: ImageView
    private lateinit var btnPlayPause: Button
    private var mediaPlayer: MediaPlayer? = null
    private var song: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        tvSongTitle = findViewById(R.id.tv_song_title)
        tvSongArtist = findViewById(R.id.tv_song_artist)
        ivAlbumArt = findViewById(R.id.iv_album_art)
        btnPlayPause = findViewById(R.id.btn_play_pause)

        song = intent.getParcelableExtra("song")

        song?.let {
            tvSongTitle.text = it.title
            tvSongArtist.text = it.artist
            // Set album art if available

            btnPlayPause.setOnClickListener {
                togglePlayPause()
            }
        }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                btnPlayPause.text = "Play"
            } else {
                it.start()
                btnPlayPause.text = "Pause"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
