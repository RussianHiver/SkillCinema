package com.example.skillcinema.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.SearchResultDTO
import com.example.skillcinema.presentation.pagingsources.PagingSourceSearchResult
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(private val pagingSourceSearchResult: PagingSourceSearchResult) {
    fun getSearchResultPager(keyword: String): Pager<Int, SearchResultDTO.Film> {
        val pagingSourceSearchResult = pagingSourceSearchResult
        pagingSourceSearchResult.inputWord(keyword)
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceSearchResult }
        )
    }

}