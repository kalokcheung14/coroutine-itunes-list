package com.kalok.simpleituneslist.repositories

import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.models.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.http.GET

abstract class ApiDataRepository {
    lateinit var api: DataApi

    var cachedAlbum = DataResult<Album>(0, ArrayList())

    fun getAlbums(): Flow<DataResult<Album>> {
        return flow {
            if (cachedAlbum.results.isEmpty()) {
                // Save albums from API as cache
                api.getAlbums().map {
                    cachedAlbum = it
                }
            }
            // Emit result
            emit(cachedAlbum)
        }
    }
}

interface DataApi {
    // Call iTunes API endpoint
    @GET("search?term=jack+johnson&entity=album")
    fun getAlbums(): Flow<DataResult<Album>>
}