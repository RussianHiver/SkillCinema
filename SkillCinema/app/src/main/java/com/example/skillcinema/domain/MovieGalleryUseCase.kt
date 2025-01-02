package com.example.skillcinema.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.di.DaggerAppComponent
import javax.inject.Inject

class MovieGalleryUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend fun getMovieGallery(id: Int, page: Int, type: String): GalleryDTO {
        return mainRepository.getGalleryPhotos(page = page, id = id, type = type)
    }

    fun getMovieGalleryPager(filmId: Int, type: String): Pager<Int, GalleryDTO.Item> {
        val pagingSourceGallery = DaggerAppComponent.create().pagingSourceGallery()
        pagingSourceGallery.inputIDandType(filmId, type)
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceGallery }
        )
    }
}