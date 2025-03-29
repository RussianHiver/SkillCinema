package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.domain.FilteredMovieUseCase

class PagingSourceSearchResult(
    val keyword: String,
    val country: String,
    val genre: String,
    val type: String,
    val order: String,
    val ratingFrom: Float,
    val ratingTo: Float,
    val yearFrom: Int,
    val yearTo: Int,
    val filteredMovieUseCase: FilteredMovieUseCase
) : PagingSource<Int, FilteredDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, FilteredDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilteredDTO.Item> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            filteredMovieUseCase.getSearchResultMovies(
                page = page,
                country = country,
                genre = genre,
                keyword = keyword,
                order = order,
                type = type,
                ratingFrom = ratingFrom,
                ratingTo = ratingTo,
                yearFrom = yearFrom,
                yearTo = yearTo
            )
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
        const val FIRST_PAGE = 1
    }
}