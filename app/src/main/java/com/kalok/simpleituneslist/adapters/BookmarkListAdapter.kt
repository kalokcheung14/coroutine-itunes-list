package com.kalok.simpleituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.ui.bookmarks.BookmarksViewModel
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel

class BookmarkListAdapter(
    albums: ArrayList<AlbumViewModel>,
    _parentViewModel: ViewModel? = null
): AlbumAdapter(
    albums,
    _parentViewModel
) {
    override fun handleRemoveBookmark(album: AlbumViewModel, position: Int) {
        _albums.remove(album)
        notifyDataSetChanged()
        // Update parent UI by posting data to parent view model
        if (_albums.isEmpty()) {
            (_parentViewModel as BookmarksViewModel).setAlbumView(_albums)
        }
    }
}