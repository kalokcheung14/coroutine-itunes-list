package com.kalok.coroutineituneslist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "collection_id")
    val trackId: Long,
    @ColumnInfo(name = "song_name")
    val trackName: String,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl60: String,
    @ColumnInfo(name = "previewUrl")
    val previewUrl: String,
)