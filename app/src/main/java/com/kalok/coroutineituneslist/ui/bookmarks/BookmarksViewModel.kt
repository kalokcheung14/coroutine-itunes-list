package com.kalok.coroutineituneslist.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.viewmodels.SongViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class BookmarksViewModel(
    private val _dbHelper: DatabaseHelper
) : ViewModel() {
    private var songs = MutableStateFlow<ArrayList<SongViewModel>>(arrayListOf())
    val songValue: StateFlow<ArrayList<SongViewModel>> = songs

    init {
        // Init mutable data value
        songs.value = ArrayList()
    }

    fun fetchData() {
        // Get database song DAO and get all songs
        viewModelScope.launch(Dispatchers.IO) {
            _dbHelper.getSongDao()?.apply {
                val songList = ArrayList<SongViewModel>()
                songList.addAll(getAll().map { song ->
                    // Encapsulate song with ViewModel
                    SongViewModel(
                        song,
                        // Set bookmarked to true since song loaded from database are all bookmarked
                        true,
                        _dbHelper
                    )
                })

                songs.update {
                    songList
                }
            }
        }
    }

    fun setSongView(songs: ArrayList<SongViewModel>) {
        this.songs.update {
            songs
        }
    }
}