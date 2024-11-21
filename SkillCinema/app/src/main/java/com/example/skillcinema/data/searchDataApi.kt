package com.example.skillcinema.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
private const val API_KEY = "X-API-KEY: 2fecb949-a607-42d0-ae91-aac652d854b8"
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
    @Headers(API_KEY)
    @GET("/api/v2.2/films/premieres")
    suspend fun getMoviePremieres(
        @Query("year") year: Int = 2024,
        @Query("month") month: String = MONTHS[1]
    ): Premieres

    @Headers(API_KEY)
    @GET("/api/v2.2/films/collections")
    suspend fun getPopular(
        @Query("type") type: String = "TOP_POPULAR_ALL",
        @Query("page") page: Int = 1
    ): Popular

    @Headers(API_KEY)
    @GET("/api/v2.2/films/filters")
    suspend fun getFilters(): GenresAndCountries

    @Headers(API_KEY)
    @GET("/api/v2.2/films")
    suspend fun getFilteredMovies(
        @Query("countries") countries: Array<Int> = arrayOf(1),
        @Query("genres") genres: Array<Int> = arrayOf(1),
        @Query("order") order: String = "RATING",
        @Query("type") type: String = "ALL",
        @Query("ratingFrom") ratingFrom: Number = 0,
        @Query("ratingTo") ratingTo: Number = 10,
        @Query("yearFrom") yearFrom: Int = 1000,
        @Query("yearTo") yearTo: Int = 3000,
        @Query("page") page: Int = 1
    ): Filtered

    @Headers(API_KEY)
    @GET("/api/v2.2/films/{id}")
    suspend fun getOneMovie(
        @Path("id") id: Int = 301
    ): MovieData

    @Headers(API_KEY)
    @GET("/api/v1/staff")
    suspend fun getFilmStaff(
        @Query("filmId") filmId: Int = 66539
    ): List<StaffItem>

    @Headers(API_KEY)
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getFilmGallery(
        @Path("id") id: Int = 263531,
        @Query("page") page: Int = 1,
        @Query("type") type: String = "STILL"
    ): Gallery

    @Headers(API_KEY)
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars(
        @Path("id") id: Int = 263531
    ): Similars

    @Headers(API_KEY)
    @GET("/api/v1/staff/{id}")
    suspend fun getIndividualStaffMember(
        @Path("id") id: Int = 66539
    ): StaffMember
}