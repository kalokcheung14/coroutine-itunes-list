package com.kalok.coroutineituneslist.repositories

interface DatabaseHelper {
    fun getSongDao(): SongDao?
}