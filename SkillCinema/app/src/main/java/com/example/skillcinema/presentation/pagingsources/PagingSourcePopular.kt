package com.example.skillcinema.presentation.pagingsources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.domain.PopularMovieUseCase

class PagingSourcePopular (val type: String, val popularMovieUseCase: PopularMovieUseCase) :
    PagingSource<Int, PopularDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, PopularDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularDTO.Item> {
        Log.d("testInside", type)

        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            popularMovieUseCase.getMoviesPopular(page = page, type = type)
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

    companion object {
        private const val FIRST_PAGE = 1
    }
}