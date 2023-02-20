package com.kalok.coroutineituneslist.repositories

import android.util.Log
import com.kalok.coroutineituneslist.models.Album
import com.kalok.coroutineituneslist.models.DataResult
import com.kalok.coroutineituneslist.utils.Result
import com.kalok.coroutineituneslist.utils.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.http.GET

abstract class ApiDataRepository {
    lateinit var api: DataApi

    private var cachedAlbum = DataResult<Album>(0, ArrayList())

    fun getAlbums(): Flow<DataResult<Album>> {
        Log.d("TAG", "getAlbums: ")
        return flow {
            Log.d("TAG", "getAlbums: flow ${cachedAlbum.results.isEmpty()}")
            if (cachedAlbum.results.isEmpty()) {
                // Save albums from API as cache
                cachedAlbum = api.getAlbums()
                // Emit result
                emit(cachedAlbum)
            } else {
                // Emit result
                emit(cachedAlbum)
            }
        }.flowOn(Dispatchers.IO)
    }
}

interface DataApi {
    // Call iTunes API endpoint
    @GET("search?term=jack+johnson&entity=album")
    suspend fun getAlbums(): DataResult<Album>
}