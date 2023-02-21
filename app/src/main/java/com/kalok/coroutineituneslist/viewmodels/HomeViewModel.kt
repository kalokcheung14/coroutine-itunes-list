package com.kalok.coroutineituneslist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.models.Song
import com.kalok.coroutineituneslist.repositories.ApiDataRepository
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.utils.asResult
import kotlinx.coroutines.launch
import com.kalok.coroutineituneslist.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val _repo: ApiDataRepository,
    private val _dbHelper: DatabaseHelper
) : ViewModel() {
    private var songs = MutableStateFlow<ArrayList<SongViewModel>>(arrayListOf())
    val songValue: StateFlow<ArrayList<SongViewModel>> = songs

    init {
        // Init mutable data value
        songs.value = ArrayList()
    }

    fun fetchData() {
        var bookmarkSongList: List<Song> = emptyList()
        var networkSongList: List<Song> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            // Get API response for songs
            _repo.getSongs().asResult().collect { result ->
                networkSongList = when(result) {
                    is Result.Success -> result.data.results
                    is Result.Error -> arrayListOf()
                }
            }

            // Subscribe to database call response to get bookmarks
            _dbHelper.getSongDao()?.apply {
                bookmarkSongList = getAll()
            }

            val bookmarkCollectionIdList = bookmarkSongList.map { it.trackId }

            // Get song list from response and replace the current list
            val songList = ArrayList<SongViewModel>()

            // add result to an array list
            songList.addAll(networkSongList.map { song ->
                // Encapsulate song with ViewModel
                SongViewModel(
                    song,
                    // Update bookmarked flag based on whether the song is also contained in the bookmarked database
                    bookmarkCollectionIdList.contains(song.trackId),
                    _dbHelper
                )
            })

            songs.update {
                songList
            }
        }
    }
}