package com.example.skillcinema.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresAndCountries(
    @Json(name = "countries") val countries: List<Country>,
    @Json(name = "genres") val genres: List<Genre>
) {
    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "country") val country: String,
        @Json(name = "id") val id: Int
    )

    @JsonClass(generateAdapter = true)
    data class Genre(
        @Json(name = "genre") val genre: String,
        @Json(name = "id") val id: Int
    )
}