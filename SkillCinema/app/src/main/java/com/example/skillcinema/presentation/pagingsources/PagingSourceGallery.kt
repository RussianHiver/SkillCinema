package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.MovieDataSourceRetrofit
import javax.inject.Inject

class PagingSourceGallery @Inject constructor(private val movieDataSourceRetrofit: MovieDataSourceRetrofit) :
    PagingSource<Int, GalleryDTO.Item>() {

    private var filmId = 263531
    private var type = "STILL"

    override fun getRefreshKey(state: PagingState<Int, GalleryDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            movieDataSourceRetrofit.searchDataApi.getFilmGallery(
                page = page,
                id = filmId,
                type = type
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

    fun inputIDandType(newId: Int, newType: String) {
        filmId = newId
        type = newType
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}