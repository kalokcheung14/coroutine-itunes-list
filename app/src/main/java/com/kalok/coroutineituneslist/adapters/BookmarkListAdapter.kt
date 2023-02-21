package com.kalok.coroutineituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.coroutineituneslist.viewmodels.BookmarksViewModel
import com.kalok.coroutineituneslist.viewmodels.SongViewModel

class BookmarkListAdapter(
    songs: ArrayList<SongViewModel>,
    _parentViewModel: ViewModel? = null
): SongAdapter(
    songs,
    _parentViewModel
) {
    override fun handleRemoveBookmark(song: SongViewModel, position: Int) {
        _songs.remove(song)
        notifyDataSetChanged()
        // Update parent UI by posting data to parent view model
        if (_songs.isEmpty()) {
            (_parentViewModel as BookmarksViewModel).setSongView(_songs)
        }
    }
}