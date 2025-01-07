package com.example.skillcinema.data

import com.example.skillcinema.entity.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PremieresDTO(
    @Json(name = "items") val items: List<Item>,
    @Json(name = "total") val total: Int
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "countries") val countries: List<Country>,
        @Json(name = "duration") val duration: Int?,
        @Json(name = "genres") val genres: List<Genre>,
        @Json(name = "kinopoiskId") override val kinopoiskId: Int,
        @Json(name = "nameEn") override val nameEn: String,
        @Json(name = "nameRu") override val nameRu: String,
        @Json(name = "posterUrl") override val posterUrl: String,
        @Json(name = "posterUrlPreview") override val posterUrlPreview: String,
        @Json(name = "year") override val year: Int
    ) : Movie<Int> {
        @JsonClass(generateAdapter = true)
        data class Country(
            @Json(name = "country") val country: String
        )

        @JsonClass(generateAdapter = true)
        data class Genre(
            @Json(name = "genre") val genre: String
        )
    }
}