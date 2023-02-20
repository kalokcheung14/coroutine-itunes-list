package com.kalok.coroutineituneslist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.models.Album
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumViewModel(
    val album: Album,
    bookmarked: Boolean,
    private val _databaseHelper: DatabaseHelper,
): ViewModel() {
    val albumName = album.collectionName
    val artworkUrl = album.artworkUrl60

    var bookmarked: Boolean = bookmarked
        set(value) {
            // Set bookmarked flag in view model
            field = value

            // Update bookmarked album to database
            viewModelScope.launch(Dispatchers.IO) {
                _databaseHelper.getAlbumDao()?.apply {
                    // Get bookmarked album from database by collection ID
                    val albums = getByCollectionId(album.collectionId)
                    if (albums.isNotEmpty()) {
                        // If album is found in bookmark
                        // update the album object with the ID for insert/delete function to find the record
                        albums[0].let {
                            album.id = it.id
                        }
                    }

                    // Insert if the album is bookmarked
                    // Otherwise delete the album from the database
                    if (field) {
                        insert(album)
                    } else {
                        delete(album)
                    }
            }
        }
    }
}