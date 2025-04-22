package com.example.skillcinema.domain

import com.example.skillcinema.data.LocalRepository
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

class DatabaseFilmsUseCase(private val localRepository: LocalRepository) {

    suspend fun insertFilm(film: FilmEntity) {
        localRepository.insertFilm(film)
    }

    fun checkTheFilm(filmId: Int, collectionID: Int): CollectionListFilms {
        return localRepository.checkTheFilm(filmId, collectionID)
    }

}