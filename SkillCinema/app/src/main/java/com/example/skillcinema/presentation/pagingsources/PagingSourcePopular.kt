package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.MovieDataSourceRetrofit
import com.example.skillcinema.data.PopularDTO
import javax.inject.Inject

class PagingSourcePopular @Inject constructor(private val movieDataSourceRetrofit: MovieDataSourceRetrofit) :
    PagingSource<Int, PopularDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, PopularDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            movieDataSourceRetrofit.searchDataApi.getPopular(page = page, type = popularType)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items,
                    prevKey = null,
                    nextKey = if (it.items.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    fun putPopularType(type: String) {
        popularType = type
    }

    companion object {
        private const val FIRST_PAGE = 1
        private lateinit var popularType: String
    }
}