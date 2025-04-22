package com.example.skillcinema.domain

import com.example.skillcinema.data.LocalRepository
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.CollectionWithFilms
import kotlinx.coroutines.flow.Flow

class DatabaseCollectionsUseCase(private val localRepository: LocalRepository) {

    suspend fun insertCollection(collection: CollectionEntity) {
        localRepository.insertCollection(collection)
    }

    suspend fun deleteFilmForCollection(film: CollectionListFilms) {
        localRepository.deleteFilmForCollection(film)
    }

    suspend fun insertFilmForCollection(filmCollection: CollectionListFilms) {
        localRepository.insertFilmForCollection(filmCollection)
    }

     fun getAllCollections(): Flow<List<CollectionWithFilms>> {
        return localRepository.getAllCollections()
    }

    suspend fun deleteCollection(collection: CollectionEntity) {
        localRepository.deleteCollection(collection)
    }

}