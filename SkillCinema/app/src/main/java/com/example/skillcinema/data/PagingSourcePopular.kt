package com.example.skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSourcePopular(val popularType: String): PagingSource<Int, Popular.Item>() {

    private val repository = Repository()
    override fun getRefreshKey(state: PagingState<Int, Popular.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Popular.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            repository.getMoviesPopular(page, popularType)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items,
                    prevKey = null,
                    nextKey = if (it.items.isEmpty()) null else page + 1
                )
            },
            onFailure = {LoadResult.Error(it)}
        )
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}