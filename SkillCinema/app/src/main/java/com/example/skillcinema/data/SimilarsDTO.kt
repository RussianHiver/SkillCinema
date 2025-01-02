package com.example.skillcinema.data


import com.example.skillcinema.entity.Similar
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarsDTO(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "total")
    val total: Int
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "filmId")
        override val filmId: Int,
        @Json(name = "nameEn")
        override val nameEn: String?,
        @Json(name = "nameOriginal")
        override val nameOriginal: String?,
        @Json(name = "nameRu")
        override val nameRu: String?,
        @Json(name = "posterUrl")
        override val posterUrl: String,
        @Json(name = "posterUrlPreview")
        override val posterUrlPreview: String,
        @Json(name = "relationType")
        override val relationType: String
    ) : Similar
}