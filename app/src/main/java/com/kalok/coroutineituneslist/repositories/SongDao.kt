package com.kalok.coroutineituneslist.repositories

import androidx.room.*
import com.kalok.coroutineituneslist.models.Song

// Data Access Object
@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    suspend fun getAll(): List<Song>

    @Query("DELETE FROM songs")
    fun clear()

    @Update
    fun updateSong(song: Song)

    @Query("SELECT * FROM songs WHERE collection_id = :collectionId")
    suspend fun getByCollectionId(collectionId: Long): List<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Delete
    fun delete(song: Song)
}