package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.MovieDataSourceRetrofit

class PagingSourceFiltered(val genre: String, val country: String) :
    PagingSource<Int, FilteredDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, FilteredDTO.Item>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilteredDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        val filters = MovieDataSourceRetrofit().searchDataApi.getFilters()
        val neededCountry = filters.countries.first { it.country == country }.id
        val neededGenre = filters.genres.first {it.genre == genre}.id

        return kotlin.runCatching {
            MovieDataSourceRetrofit().searchDataApi.getFilteredMovies(
                page = page, countries = arrayOf(neededCountry),
                genres = arrayOf(neededGenre)
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