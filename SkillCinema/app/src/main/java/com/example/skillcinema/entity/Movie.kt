package com.example.skillcinema.entity

interface Movie<T>{
    val kinopoiskId: Int
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String
    val posterUrlPreview: String
    val year: T
}