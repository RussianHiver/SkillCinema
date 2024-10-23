package com.example.skillcinema.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieData(
    @Json(name = "completed") val completed: Boolean?,
    @Json(name = "countries") val countries: List<Country>,
    @Json(name = "coverUrl") val coverUrl: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "editorAnnotation") val editorAnnotation: String?,
    @Json(name = "endYear") val endYear: Int?,
    @Json(name = "filmLength") val filmLength: Int?,
    @Json(name = "genres") val genres: List<Genre>,
    @Json(name = "has3D") val has3D: Boolean?,
    @Json(name = "hasImax") val hasImax: Boolean?,
    @Json(name = "imdbId") val imdbId: String?,
    @Json(name = "isTicketsAvailable") val isTicketsAvailable: Boolean,
    @Json(name = "kinopoiskHDId") val kinopoiskHDId: String?,
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "lastSync") val lastSync: String,
    @Json(name = "logoUrl") val logoUrl: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "productionStatus") val productionStatus: String?,
    @Json(name = "ratingAgeLimits") val ratingAgeLimits: String?,
    @Json(name = "ratingKinopoisk") val ratingKinopoisk: Double?,
    @Json(name = "reviewsCount") val reviewsCount: Int,
    @Json(name = "serial") val serial: Boolean?,
    @Json(name = "shortDescription") val shortDescription: String?,
    @Json(name = "slogan") val slogan: String?,
    @Json(name = "startYear") val startYear: Int?,
    @Json(name = "type") val type: String,
    @Json(name = "webUrl") val webUrl: String,
    @Json(name = "year") val year: Int?
) {
    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "country") val country: String
    )

    @JsonClass(generateAdapter = true)
    data class Genre(
        @Json(name = "genre") val genre: String
    )
}