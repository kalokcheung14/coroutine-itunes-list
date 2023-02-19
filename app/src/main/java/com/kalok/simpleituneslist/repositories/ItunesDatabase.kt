package com.kalok.simpleituneslist.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kalok.simpleituneslist.models.Album

@Database(entities = [Album::class], version = 1)
abstract class ItunesDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}