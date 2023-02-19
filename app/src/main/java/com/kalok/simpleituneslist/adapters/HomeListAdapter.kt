package com.kalok.simpleituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel

class HomeListAdapter(
    albums: ArrayList<AlbumViewModel>,
    _parentViewModel: ViewModel? = null
): AlbumAdapter(
    albums,
    _parentViewModel
)  {
    override fun handleRemoveBookmark(album: AlbumViewModel, position: Int) = notifyItemChanged(position)
}