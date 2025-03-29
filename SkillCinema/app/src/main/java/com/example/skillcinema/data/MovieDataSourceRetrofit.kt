package com.example.skillcinema.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Calendar
import javax.inject.Inject

class MovieDataSourceRetrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val searchDataApi: SearchDataApi = retrofit.create(SearchDataApi::class.java)

    interface SearchDataApi {
        @Headers(API_KEY)
        @GET("/api/v2.2/films/premieres")
        suspend fun getMoviePremieres(
            @Query("year") year: Int = 2025,
            @Query("month") month: String = MONTHS[currentMonth]
        ): PremieresDTO

        @Headers(API_KEY)
        @GET("/api/v2.2/films/collections")
        suspend fun getPopular(
            @Query("type") type: String,
            @Query("page") page: Int
        ): PopularDTO

        @Headers(API_KEY)
        @GET("/api/v2.2/films/filters")
        suspend fun getFilters(): GenresAndCountriesDTO

        @Headers(API_KEY)
        @GET("/api/v2.2/films")
        suspend fun getFilteredMovies(
            @Query("countries") countries: Array<Int>,
            @Query("genres") genres: Array<Int>,
            @Query("order") order: String = "RATING",
            @Query("type") type: String = "ALL",
            @Query("ratingFrom") ratingFrom: Number = 0,
            @Query("ratingTo") ratingTo: Number = 10,
            @Query("yearFrom") yearFrom: Int = 1998,
            @Query("yearTo") yearTo: Int = 2024,
            @Query("page") page: Int = 1,
            @Query("keyword") keyword: String = ""
        ): FilteredDTO

        @Headers(API_KEY)
        @GET("/api/v2.2/films/{id}")
        suspend fun getOneMovie(
            @Path("id") id: Int = 301
        ): MovieDataDTO

        @Headers(API_KEY)
        @GET("/api/v1/staff")
        suspend fun getFilmStaff(
            @Query("filmId") filmId: Int = 66539
        ): List<StaffListDTO>

        @Headers(API_KEY)
        @GET("/api/v2.2/films/{id}/images")
        suspend fun getFilmGallery(
            @Path("id") id: Int = 263531,
            @Query("page") page: Int = 1,
            @Query("type") type: String = "STILL"
        ): GalleryDTO

        @Headers(API_KEY)
        @GET("/api/v2.2/films/{id}/similars")
        suspend fun getSimilars(
            @Path("id") id: Int = 263531
        ): SimilarsDTO

        @Headers(API_KEY)
        @GET("/api/v1/staff/{id}")
        suspend fun getIndividualStaffMember(
            @Path("id") id: Int = 66539
        ): StaffMemberDTO


        @Headers(API_KEY)
        @GET("/api/v2.2/films/{id}/seasons")
        suspend fun getSeasonsAndEpisodes(
            @Path("id") id: Int
        ): SeasonsAndEpisodesDTO

    }

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech"
        private const val API_KEY = "X-API-KEY: 2fecb949-a607-42d0-ae91-aac652d854b8"
        val MONTHS = arrayOf(
            "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
            "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        )
        private val calendar = Calendar.getInstance()
        private val currentMonth = calendar.get(Calendar.MONTH) + 1
    }
}