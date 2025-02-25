package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.SimilarsDTO
import javax.inject.Inject

class SimilarMoviesUseCase(private val mainRepository: MainRepository) {

    suspend fun getSimilars(id: Int): SimilarsDTO {
        return mainRepository.getSimilarMovies(id = id)
    }

}