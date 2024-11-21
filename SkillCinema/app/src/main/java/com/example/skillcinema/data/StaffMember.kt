package com.example.skillcinema.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffMember(
    @Json(name = "age")
    val age: Int?,
    @Json(name = "films")
    val films: List<Film>,
    @Json(name = "nameEn")
    val nameEn: String?,
    @Json(name = "nameRu")
    val nameRu: String?,
    @Json(name = "personId")
    val personId: Int,
    @Json(name = "posterUrl")
    val posterUrl: String,
    @Json(name = "profession")
    val profession: String?,
    @Json(name = "sex")
    val sex: String?
) {
    @JsonClass(generateAdapter = true)
    data class Film(
        @Json(name = "description")
        val description: String?,
        @Json(name = "filmId")
        val filmId: Int,
        @Json(name = "general")
        val general: Boolean,
        @Json(name = "nameEn")
        val nameEn: String?,
        @Json(name = "nameRu")
        val nameRu: String?,
        @Json(name = "professionKey")
        val professionKey: String,
        @Json(name = "rating")
        val rating: String?
    )
}