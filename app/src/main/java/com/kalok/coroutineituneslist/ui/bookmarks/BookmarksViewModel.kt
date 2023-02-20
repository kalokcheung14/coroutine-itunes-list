package com.kalok.coroutineituneslist.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.viewmodels.AlbumViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class BookmarksViewModel(
    private val _dbHelper: DatabaseHelper
) : ViewModel() {
    private var albums = MutableStateFlow<ArrayList<AlbumViewModel>>(arrayListOf())
    val albumValue: StateFlow<ArrayList<AlbumViewModel>> = albums

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchData() {
        // Get database album DAO and get all albums
        viewModelScope.launch(Dispatchers.IO) {
            _dbHelper.getAlbumDao()?.apply {
                val albumList = ArrayList<AlbumViewModel>()
                albumList.addAll(getAll().map { album ->
                    // Encapsulate album with ViewModel
                    AlbumViewModel(
                        album,
                        // Set bookmarked to true since albums loaded from database are all bookmarked
                        true,
                        _dbHelper
                    )
                })

                albums.update {
                    albumList
                }
            }
        }
    }

    fun setAlbumView(albums: ArrayList<AlbumViewModel>) {
        this.albums.update {
            albums
        }
    }
}