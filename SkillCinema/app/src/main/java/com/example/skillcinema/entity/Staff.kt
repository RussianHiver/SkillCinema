package com.example.skillcinema.entity

import com.squareup.moshi.Json

interface Staff {
    val staffId: Int
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String
}