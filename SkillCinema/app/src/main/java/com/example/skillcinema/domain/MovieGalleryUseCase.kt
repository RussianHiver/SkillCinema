package com.example.skillcinema.domain

import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.MainRepository

class MovieGalleryUseCase(private val mainRepository: MainRepository) {

    suspend fun getMovieGallery(id: Int, page: Int, type: String): GalleryDTO {
        return mainRepository.getGalleryPhotos(page = page, id = id, type = type)
    }
}