package com.kalok.simpleituneslist.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.utils.asResult
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.kalok.simpleituneslist.utils.Result

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
        viewModelScope.launch {
            _dbHelper.getAlbumDao()?.apply {
                getAll()
                    .asResult()
                    .collect { result ->
                        // Get album list from response and replace the current list
                        val albumList = ArrayList<AlbumViewModel>()
                        if(result is Result.Success) {
                            // Add all loaded albums to an array list
                            albumList.addAll(result.data.map { album ->
                                // Encapsulate album with ViewModel
                                AlbumViewModel(
                                    album,
                                    // Set bookmarked to true since albums loaded from database are all bookmarked
                                    true,
                                    _dbHelper
                                )
                            })
                        }

                        albums.update {
                            albumList
                        }
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