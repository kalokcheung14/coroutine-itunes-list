package com.kalok.coroutineituneslist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.models.Album
import com.kalok.coroutineituneslist.repositories.ApiDataRepository
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import com.kalok.coroutineituneslist.utils.asResult
import com.kalok.coroutineituneslist.viewmodels.AlbumViewModel
import kotlinx.coroutines.launch
import com.kalok.coroutineituneslist.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val _repo: ApiDataRepository,
    private val _dbHelper: DatabaseHelper
) : ViewModel() {
    private var albums = MutableStateFlow<ArrayList<AlbumViewModel>>(arrayListOf())
    val albumValue: StateFlow<ArrayList<AlbumViewModel>> = albums

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchData() {
        var bookmarkAlbumList: List<Album> = emptyList()
        var networkAlbumList: List<Album> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            // Get API response for albums
            _repo.getAlbums().asResult().collect { result ->
                networkAlbumList = when(result) {
                    is Result.Success -> result.data.results
                    is Result.Error -> arrayListOf()
                }
            }

            // Subscribe to database call response to get bookmarks
            _dbHelper.getAlbumDao()?.apply {
                bookmarkAlbumList = getAll()
            }

            val bookmarkCollectionIdList = bookmarkAlbumList.map { it.collectionId }

            // Get album list from response and replace the current list
            val albumList = ArrayList<AlbumViewModel>()

            // add result to an array list
            albumList.addAll(networkAlbumList.map { album ->
                // Encapsulate album with ViewModel
                AlbumViewModel(
                    album,
                    // Update bookmarked flag based on whether the album is also contained in the bookmarked database
                    bookmarkCollectionIdList.contains(album.collectionId),
                    _dbHelper
                )
            })

            albums.update {
                albumList
            }
        }
    }
}