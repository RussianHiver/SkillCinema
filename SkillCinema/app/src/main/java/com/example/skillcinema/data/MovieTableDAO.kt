package com.example.skillcinema.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.skillcinema.entity.MovieTable
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieTableDAO {

    @Query("SELECT * FROM movie")
    fun getAll(): Flow<List<MovieTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieTable: MovieTable)

    @Update
    fun update(movieTable: MovieTable)

    @Delete
    fun delete(movieTable: MovieTable)

}