package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.domain.FilteredMovieUseCase

class PagingSourceFiltered (val country: String, val genre: String, val filteredMovieUseCase: FilteredMovieUseCase) :
    PagingSource<Int, FilteredDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, FilteredDTO.Item>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilteredDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            filteredMovieUseCase.getMoviesFiltered(
                page = page, country = country,
                genre = genre
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
        private const val FIRST_PAGE = 1
    }
}