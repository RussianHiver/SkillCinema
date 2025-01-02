package com.example.skillcinema.domain

import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.data.PremieresDTO
import com.example.skillcinema.data.SearchResultDTO
import javax.inject.Inject

class PremierMovieUseCase @Inject constructor (private val mainRepository: MainRepository) {

    suspend fun getMoviesPremiers(): PremieresDTO {
        return mainRepository.getMoviesPremiers()
    }

}