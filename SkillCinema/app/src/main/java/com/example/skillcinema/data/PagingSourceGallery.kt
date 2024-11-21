package com.example.skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSourceGallery(val filmId: Int, val type: String): PagingSource<Int, Gallery.Item>() {

    val repository = Repository()
    override fun getRefreshKey(state: PagingState<Int, Gallery.Item>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery.Item> {
        val page = params.key ?: FIRST_PAGE

        return kotlin.runCatching { repository.getGalleryPhotos(page, filmId, type) }.fold(
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
        const val FIRST_PAGE = 1
    }
}