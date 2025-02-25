package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.PremieresDTO
import javax.inject.Inject

class PremierMovieUseCase  (private val mainRepository: MainRepository) {

    suspend fun getMoviesPremiers(): PremieresDTO {
        return mainRepository.getMoviesPremiers()
    }

}