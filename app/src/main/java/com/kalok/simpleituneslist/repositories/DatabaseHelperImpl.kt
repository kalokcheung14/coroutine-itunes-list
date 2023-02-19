package com.kalok.simpleituneslist.repositories

import android.content.Context
import androidx.room.Room

class DatabaseHelperImpl(context: Context): DatabaseHelper {
    private val _databaseName: String = "albums_db"
    private var _db: ItunesDatabase? = null

    init {
        // Get DB instance
        _db = Room.databaseBuilder(
            context,
            ItunesDatabase::class.java,
            _databaseName
        ).build()
    }

    override fun getAlbumDao(): AlbumDao? {
        // Get album DAO
        return _db?.albumDao()
    }
}