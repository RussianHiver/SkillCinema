package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.MovieDataSourceRetrofit
import com.example.skillcinema.data.SearchResultDTO
import javax.inject.Inject

class PagingSourceSearchResult @Inject constructor(private val movieDataSourceRetrofit: MovieDataSourceRetrofit) :
    PagingSource<Int, SearchResultDTO.Film>() {

    private var word = "Брат"

    override fun getRefreshKey(state: PagingState<Int, SearchResultDTO.Film>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResultDTO.Film> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching { movieDataSourceRetrofit.searchDataApi.getSearchResult(keyword = word, page = page) }
            .fold(
                onSuccess = {
                    LoadResult.Page(
                        data = it.films,
                        prevKey = null,
                        nextKey = if (it.films.isEmpty()) null else page + 1
                    )
                },
                onFailure = { LoadResult.Error(it) }
            )
    }

    fun inputWord(newWord: String) {
        word = newWord
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}