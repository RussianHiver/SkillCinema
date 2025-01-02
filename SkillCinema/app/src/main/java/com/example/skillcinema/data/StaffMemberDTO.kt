package com.example.skillcinema.data


import com.example.skillcinema.entity.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffMemberDTO(
    @Json(name = "age")
    val age: Int?,
    @Json(name = "films")
    val films: List<Film>,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "personId")
    override val staffId: Int,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "profession")
    val profession: String?,
    @Json(name = "sex")
    val sex: String?
) : Staff {
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