package com.example.skillcinema

import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.Premieres
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
val MONTHS = arrayOf("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
    "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER")
object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val searchDataApi: SearchDataApi = retrofit.create(SearchDataApi::class.java)
}

interface SearchDataApi {
    @Headers("X-API-KEY: 2fecb949-a607-42d0-ae91-aac652d854b8")
    @GET("/api/v2.2/films/premieres")
    suspend fun getMoviePremieres(
        @Query("year") year: Int = 2024,
        @Query("month") month: String = MONTHS[1]
    ): Premieres

    @GET("/api/v2.2/films/collections")
    suspend fun getPopular(
        @Query("type") type: String = "TOP_POPULAR_ALL",
        @Query("page") page: Int = 1
    ): Popular
}