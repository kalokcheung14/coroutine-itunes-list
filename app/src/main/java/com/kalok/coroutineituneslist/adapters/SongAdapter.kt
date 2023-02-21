package com.kalok.coroutineituneslist.adapters

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kalok.coroutineituneslist.databinding.SongItemRowBinding
import com.kalok.coroutineituneslist.utils.MediaPlayerUtils
import com.kalok.coroutineituneslist.viewmodels.SongViewModel

abstract class SongAdapter(
    protected var _songs: ArrayList<SongViewModel>,
    protected val _parentViewModel: ViewModel? = null
): RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private var _player: MediaPlayer? = null
    private var _playingSong: SongViewModel? = null

    init {
        // Retain recycler view scroll position when fragment reattached
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    inner class ViewHolder(private val _binding: SongItemRowBinding): RecyclerView.ViewHolder(_binding.root) {
        fun bind(song: SongViewModel, position: Int) {
            // Bind data to row item view
            with(_binding) {
                this.song = song
                this.executePendingBindings()
                // Set bookmark icon according to bookmark flag in song
                song.bookmarked.let { bookmarked ->
                    // Set on click listener for bookmark icon
                    bookmarkImageView.setOnClickListener {
                        // Update bookmark flag to viewModel and DB
                        song.bookmarked = !bookmarked

                        if (!bookmarked) {
                            // Update display
                            notifyItemChanged(position)
                        } else {
                            // If song is bookmarked, remove the song from bookmark and set the icon to outline
                            MediaPlayerUtils.releasePlayer()
                            handleRemoveBookmark(song, position)
                        }
                    }
                }

                song.playing.let { playing ->
                    // Play preview action
                    playImageView.setOnClickListener {
                        MediaPlayerUtils.releasePlayer()

                        if (!playing) {
                            // Play new preview using new player
                            MediaPlayerUtils.play(song)
                        }

                        // Invert playing flag
                        song.playing = !playing

                        // Update display
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    abstract fun handleRemoveBookmark(song: SongViewModel, position: Int)

    fun setDataset(data : ArrayList<SongViewModel>) {
        // Check data differences
        val diffCallback = ItemListDiffUtil(_songs, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        // Update data when they are different
        diffResult.dispatchUpdatesTo(this)

        // Clear song and fill data set
        _songs.clear()
        _songs.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Pass parent and set attachToParent to false to ensures the constraints of the parent are used
        val binding = SongItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(_songs[position], position)

    override fun getItemCount(): Int = _songs.size
}