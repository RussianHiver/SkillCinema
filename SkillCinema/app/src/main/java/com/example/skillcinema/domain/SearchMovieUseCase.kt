package com.example.skillcinema.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.SearchResultDTO
import com.example.skillcinema.di.DaggerAppComponent
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor() {

    fun getSearchResultPager(keyword: String): Pager<Int, SearchResultDTO.Film> {
        val pagingSourceSearchResult = DaggerAppComponent.create().pagingSourceSearchResultPager()
        pagingSourceSearchResult.inputWord(keyword)
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceSearchResult }
        )
    }

}