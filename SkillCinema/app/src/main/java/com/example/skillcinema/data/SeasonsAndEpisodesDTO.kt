package com.example.skillcinema.data


import com.example.skillcinema.entity.Episodes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonsAndEpisodesDTO(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "total")
    val total: Int
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "episodes")
        val episodes: List<Episode>,
        @Json(name = "number")
        val number: Int
    ) {
        @JsonClass(generateAdapter = true)
        data class Episode(
            @Json(name = "episodeNumber")
            override val episodeNumber: Int,
            @Json(name = "nameEn")
            override val nameEn: String?,
            @Json(name = "nameRu")
            override val nameRu: String?,
            @Json(name = "releaseDate")
            override val releaseDate: String?,
            @Json(name = "seasonNumber")
            override val seasonNumber: Int,
            @Json(name = "synopsis")
            override val synopsis: String?
        ) : Episodes
    }
}