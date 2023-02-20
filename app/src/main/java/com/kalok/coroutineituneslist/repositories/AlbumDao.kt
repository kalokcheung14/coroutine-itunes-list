package com.kalok.coroutineituneslist.repositories

import androidx.room.*
import com.kalok.coroutineituneslist.models.Album

// Data Access Object
@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    suspend fun getAll(): List<Album>

    @Query("DELETE FROM albums")
    fun clear()

    @Update
    fun updateAlbum(album: Album)

    @Query("SELECT * FROM albums WHERE collection_id = :collectionId")
    suspend fun getByCollectionId(collectionId: Long): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Delete
    fun delete(album: Album)
}