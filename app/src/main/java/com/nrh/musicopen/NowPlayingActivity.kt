package com.nrh.musicopen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
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
            val albumArt = getAlbumArt(it.path)
            if (albumArt != null) {
                ivAlbumArt.setImageBitmap(albumArt)
            } else {
                ivAlbumArt.setImageResource(R.drawable.default_album_art) // Use a default image if no album art is available
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(it.path)
                prepare()
                start()
            }

            btnPlayPause.text = if (mediaPlayer?.isPlaying == true) "Pause" else "Play"
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

    private fun getAlbumArt(filePath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val art = retriever.embeddedPicture
            if (art != null) {
                BitmapFactory.decodeByteArray(art, 0, art.size)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
