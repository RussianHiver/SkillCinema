package com.example.skillcinema.data


import com.example.skillcinema.entity.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryDTO(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalPages")
    val totalPages: Int
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "imageUrl")
        override val imageUrl: String,
        @Json(name = "previewUrl")
        override val previewUrl: String
    ) : Image
}