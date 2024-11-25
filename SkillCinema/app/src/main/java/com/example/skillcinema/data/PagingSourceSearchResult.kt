package com.example.skillcinema.data

import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSourceSearchResult(val keyword: String) : PagingSource<Int, SearchResult.Item>() {

    val repository = Repository()
    override fun getRefreshKey(state: PagingState<Int, SearchResult.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult.Item> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching { repository.getSearchResultsMovies(page, keyword) }.fold(
            onSuccess = {
                LoadResult.Page (
                    data = it.items,
                    prevKey = null,
                    nextKey = if (it.items.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
    }
}