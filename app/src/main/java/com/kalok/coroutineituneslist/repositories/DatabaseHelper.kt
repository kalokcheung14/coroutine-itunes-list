package com.kalok.coroutineituneslist.repositories

interface DatabaseHelper {
    fun getAlbumDao(): AlbumDao?
}