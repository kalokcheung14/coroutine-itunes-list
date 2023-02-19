package com.kalok.simpleituneslist.repositories

interface DatabaseHelper {
    fun getAlbumDao(): AlbumDao?
}