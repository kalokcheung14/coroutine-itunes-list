package com.kalok.coroutineituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.coroutineituneslist.viewmodels.SongViewModel

class HomeListAdapter(
    songs: ArrayList<SongViewModel>,
    _parentViewModel: ViewModel? = null
): SongAdapter(
    songs,
    _parentViewModel
)  {
    override fun handleRemoveBookmark(song: SongViewModel, position: Int) = notifyItemChanged(position)
}