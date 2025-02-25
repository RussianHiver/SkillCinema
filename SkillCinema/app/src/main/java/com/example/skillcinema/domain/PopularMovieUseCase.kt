package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.PopularDTO
import javax.inject.Inject

class PopularMovieUseCase  (private val mainRepository: MainRepository) {

    suspend fun getMoviesPopular(page: Int, type: String): PopularDTO {
        return mainRepository.getMoviesPopular(page = page, type = type)
    }
}