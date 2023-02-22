package com.kalok.coroutineituneslist.utils

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.kalok.coroutineituneslist.viewmodels.SongViewModel

object MediaPlayerUtils {
    var playingSong: SongViewModel? = null
    var onCompletionListener: OnCompletionListener? = null

    fun interface OnCompletionListener {
        fun onPlayerCompleted()
    }

    val player: MediaPlayer
        get() = _player

    private val _player = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        setOnCompletionListener {
            stopSong()
            onCompletionListener?.onPlayerCompleted()
        }
    }

    // Play preview from URL
    fun play(song: SongViewModel) {
        player.apply {
            setDataSource(song.previewUrl)
            prepare()
            start()
        }

        // Save current playing song
        playingSong = song
    }

    fun stopSong() {
        playingSong?.playing = false
        releasePlayer()
    }

    fun releasePlayer() {
        // Stop and release player if it is playing
        // Release player
        if (player.isPlaying)
            player.stop()
        player.reset()
        // Reset playing song icon
        playingSong?.playing = false
        playingSong = null
    }

}