package com.example.skillcinema.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.CollectionWithFilms
import com.example.skillcinema.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieTableDAO {
    @Transaction
    @Query("SELECT * FROM collection_entity" )
    fun getAll(): Flow<List<CollectionWithFilms>>

    @Query("SELECT * FROM collection_film WHERE film_id LIKE :filmId AND collection_id LIKE :collectionId")
    fun checkTheFilm(filmId: Int, collectionId: Int): CollectionListFilms

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: FilmEntity) // вставлять

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(table: CollectionEntity) // вставлять

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmForCollection(table: CollectionListFilms)

    @Delete
    suspend fun deleteFilm(table: FilmEntity) // удаление данных из БД
    @Delete
    suspend fun deleteFilmForCollection(table: CollectionListFilms)
    @Delete
    suspend fun deleteCollection(table: CollectionEntity)

}