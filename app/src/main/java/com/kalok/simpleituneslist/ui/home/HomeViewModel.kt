package com.kalok.simpleituneslist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.ApiDataRepository
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.utils.asResult
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.kalok.simpleituneslist.utils.Result

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
        viewModelScope.launch {
            // Get API response for albums
            _repo.getAlbums().asResult().collect { result ->
                networkAlbumList = when(result) {
                    is Result.Success -> result.data.results
                    is Result.Error -> arrayListOf()
                }
            }

            // Subscribe to database call response to get bookmarks
            _dbHelper.getAlbumDao()?.apply {
                getAll()
                    .collect { albumList ->
                        bookmarkAlbumList = albumList
                    }
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