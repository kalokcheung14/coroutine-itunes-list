package com.kalok.coroutineituneslist.repositories

import android.util.Log
import com.kalok.coroutineituneslist.models.Song
import com.kalok.coroutineituneslist.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.http.GET
import retrofit2.http.Query

abstract class ApiDataRepository {
    lateinit var api: DataApi

    fun getSongs(keyword: String): Flow<DataResult<Song>> {
        Log.d("TAG", "getSongs: ")
        return flow {
            // Emit result
            emit(api.getSongs(keyword))
        }.flowOn(Dispatchers.IO)
    }
}

interface DataApi {
    // Call iTunes API endpoint
    @GET("search?entity=song")
    suspend fun getSongs(@Query("term") keyword: String): DataResult<Song>
}