package com.kalok.simpleituneslist.repositories

import androidx.room.*
import com.kalok.simpleituneslist.models.Album
import kotlinx.coroutines.flow.Flow

// Data Access Object
@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    fun getAll(): Flow<List<Album>>

    @Query("DELETE FROM albums")
    fun clear()

    @Update
    fun updateAlbum(album: Album)

    @Query("SELECT * FROM albums WHERE collection_id = :collectionId")
    fun getByCollectionId(collectionId: Long): Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Delete
    fun delete(album: Album)
}