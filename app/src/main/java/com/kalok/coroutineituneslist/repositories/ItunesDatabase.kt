package com.kalok.coroutineituneslist.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kalok.coroutineituneslist.models.Song

@Database(entities = [Song::class], version = 1)
abstract class ItunesDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
}