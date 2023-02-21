package com.kalok.coroutineituneslist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalok.coroutineituneslist.models.Song
import com.kalok.coroutineituneslist.repositories.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongViewModel(
    private val _song: Song,
    bookmarked: Boolean,
    private val _databaseHelper: DatabaseHelper,
): ViewModel() {
    val songName = _song.trackName
    val artworkUrl = _song.artworkUrl60

    var bookmarked: Boolean = bookmarked
        set(value) {
            // Set bookmarked flag in view model
            field = value

            // Update bookmarked song to database
            viewModelScope.launch(Dispatchers.IO) {
                _databaseHelper.getSongDao()?.apply {
                    // Get bookmarked song from database by collection ID
                    val songs = getByCollectionId(_song.trackId)
                    if (songs.isNotEmpty()) {
                        // If song is found in bookmark
                        // update the song object with the ID for insert/delete function to find the record
                        songs[0].let {
                            _song.id = it.id
                        }
                    }

                    // Insert if the song is bookmarked
                    // Otherwise delete the song from the database
                    if (field) {
                        insert(_song)
                    } else {
                        delete(_song)
                    }
            }
        }
    }
}