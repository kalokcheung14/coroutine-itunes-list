package com.kalok.coroutineituneslist.repositories

import android.util.Log
import com.kalok.coroutineituneslist.models.Song
import com.kalok.coroutineituneslist.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.http.GET

abstract class ApiDataRepository {
    lateinit var api: DataApi

    private var cachedSong = DataResult<Song>(0, ArrayList())

    fun getSongs(): Flow<DataResult<Song>> {
        Log.d("TAG", "getSongs: ")
        return flow {
            Log.d("TAG", "getSongs: flow ${cachedSong.results.isEmpty()}")
            if (cachedSong.results.isEmpty()) {
                // Save songs from API as cache
                cachedSong = api.getSongs()
            }

            // Emit result
            emit(cachedSong)
        }.flowOn(Dispatchers.IO)
    }
}

interface DataApi {
    // Call iTunes API endpoint
    @GET("search?term=jack+johnson&entity=song")
    suspend fun getSongs(): DataResult<Song>
}