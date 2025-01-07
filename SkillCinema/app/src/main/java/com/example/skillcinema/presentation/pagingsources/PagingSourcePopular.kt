package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.MovieDataSourceRetrofit
import com.example.skillcinema.data.PopularDTO

class PagingSourcePopular (val popularType: String) : PagingSource<Int, PopularDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, PopularDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            MovieDataSourceRetrofit().searchDataApi.getPopular(page = page, type = popularType)
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