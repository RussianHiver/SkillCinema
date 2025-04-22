package com.example.skillcinema.data

import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.CollectionWithFilms
import com.example.skillcinema.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository(private val movieTableDAO: MovieTableDAO) {

    suspend fun insertCollection(collection: CollectionEntity) {
        movieTableDAO.insertCollection(collection)
    }

    suspend fun deleteCollection(collection: CollectionEntity) {
        movieTableDAO.deleteCollection(collection)
    }

    suspend fun deleteFilmForCollection(film: CollectionListFilms) {
        movieTableDAO.deleteFilmForCollection(film)
    }

    suspend fun insertFilm(film: FilmEntity) {
        movieTableDAO.insert(film)
    }

    suspend fun insertFilmForCollection(filmCollection: CollectionListFilms) {
        movieTableDAO.insertFilmForCollection(filmCollection)
    }

    fun getAllCollections(): Flow<List<CollectionWithFilms>> {
        return movieTableDAO.getAll()
    }

    fun checkTheFilm(filmId: Int, collectionID: Int): CollectionListFilms {
        return movieTableDAO.checkTheFilm(filmId, collectionID)
    }

}