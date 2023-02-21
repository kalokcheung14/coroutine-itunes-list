package com.kalok.coroutineituneslist.repositories

import android.content.Context
import androidx.room.Room

class DatabaseHelperImpl(context: Context): DatabaseHelper {
    private val _databaseName: String = "songs_db"
    private var _db: ItunesDatabase? = null

    init {
        // Get DB instance
        _db = Room.databaseBuilder(
            context,
            ItunesDatabase::class.java,
            _databaseName
        ).build()
    }

    override fun getSongDao(): SongDao? {
        // Get song DAO
        return _db?.songDao()
    }
}