package com.example.skillcinema.data

import com.example.skillcinema.entity.CountryFilter
import com.example.skillcinema.entity.GenreFilter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresAndCountriesDTO(
    @Json(name = "countries") val countries: List<Country>,
    @Json(name = "genres") val genres: List<Genre>
) {
    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "country") override val country: String,
        @Json(name = "id") override val id: Int
    ) : CountryFilter

    @JsonClass(generateAdapter = true)
    data class Genre(
        @Json(name = "genre") override val genre: String,
        @Json(name = "id") override val id: Int
    ) : GenreFilter
}