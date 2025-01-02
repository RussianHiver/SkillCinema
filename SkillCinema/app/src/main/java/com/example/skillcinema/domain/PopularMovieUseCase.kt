package com.example.skillcinema.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.di.DaggerAppComponent
import javax.inject.Inject

class PopularMovieUseCase @Inject constructor (private val mainRepository: MainRepository) {

    suspend fun getMoviesPopular(page: Int, type: String): PopularDTO {
        return mainRepository.getMoviesPopular(page = page, type = type)
    }

    fun getMoviesPopularPager(popularType: String): Pager<Int, PopularDTO.Item> {
        val pagingSourcePopular = DaggerAppComponent.create().pagingSourcePopular()
        pagingSourcePopular.inputPopularType(popularType)
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { pagingSourcePopular }
        )
    }

}