package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.MovieDataDTO
import javax.inject.Inject

class OneMovieUseCase (private val mainRepository: MainRepository) {

    suspend fun getOneMovie(id: Int): MovieDataDTO {
        return mainRepository.getOneMovie(id)
    }

}