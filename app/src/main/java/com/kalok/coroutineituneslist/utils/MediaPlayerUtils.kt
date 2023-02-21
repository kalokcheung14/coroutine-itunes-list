package com.kalok.coroutineituneslist.utils

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.kalok.coroutineituneslist.viewmodels.SongViewModel

object MediaPlayerUtils {
    var playingSong: SongViewModel? = null
    private var onCompletionListener: OnCompletionListener? = null

    fun interface OnCompletionListener {
        fun callback()
    }

    var player: MediaPlayer? = null

    private val _player = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        // Release player when preview ended
        setOnCompletionListener {
            playingSong?.playing = false
            releasePlayer()
            // Call on completion callback
            onCompletionListener?.callback()
        }
    }

    // Play preview from URL
    fun play(song: SongViewModel) {
        player = _player
        player?.apply {
            setDataSource(song.previewUrl)
            prepare()
            start()
        }

        // Save current playing song
        playingSong = song
    }

    fun setOnCompletionListener(callback: OnCompletionListener) {
        this.onCompletionListener = callback
    }

    fun removeOnCompletionListener() {
        this.onCompletionListener = null
    }

    fun releasePlayer() {
        // Stop and release player if it is playing
        if (player != null) {
            // Release player
            if (player?.isPlaying == true)
                player?.stop()
                player?.reset()
                player = null
            // Reset playing song icon
            playingSong?.playing = false
            playingSong = null
        }
    }

}