package com.example.skillcinema.presentation.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.domain.MovieGalleryUseCase

class PagingSourceGallery (val type: String, val filmId: Int, val movieGalleryUseCase: MovieGalleryUseCase) :
    PagingSource<Int, GalleryDTO.Item>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryDTO.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryDTO.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching {
            movieGalleryUseCase.getMovieGallery(
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
    private companion object {
        const val FIRST_PAGE = 1
    }
}