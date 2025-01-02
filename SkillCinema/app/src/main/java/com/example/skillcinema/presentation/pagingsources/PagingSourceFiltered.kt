package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.MovieDataSourceRetrofit
import javax.inject.Inject

class PagingSourceFiltered @Inject constructor(private val movieDataSourceRetrofit: MovieDataSourceRetrofit) :
    PagingSource<Int, FilteredDTO.Item>() {

    private var country = "США"
    private var genre = "боевик"

    override fun getRefreshKey(state: PagingState<Int, FilteredDTO.Item>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilteredDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            movieDataSourceRetrofit.searchDataApi.getFilteredMovies(page = page)
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

    fun inputGenreAndCountryType(newCountry: String, newGenre: String) {
        country = newCountry
        genre = newGenre
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}