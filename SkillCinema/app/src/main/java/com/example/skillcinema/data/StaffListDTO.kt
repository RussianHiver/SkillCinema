package com.example.skillcinema.data


import com.example.skillcinema.entity.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffListDTO(
    @Json(name = "description")
    val description: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "professionKey")
    val professionKey: String,
    @Json(name = "professionText")
    val professionText: String,
    @Json(name = "staffId")
    override val staffId: Int
) : Staff