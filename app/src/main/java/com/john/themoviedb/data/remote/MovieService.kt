package com.john.themoviedb.data.remote

import com.john.themoviedb.BuildConfig
import com.john.themoviedb.data.remote.models.ApiResponse
import com.john.themoviedb.data.remote.models.BaseApiResponse
import com.john.themoviedb.data.remote.models.MovieApiResponse
import com.john.themoviedb.models.Movie
import com.john.themoviedb.models.Review
import com.john.themoviedb.models.Trailer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {

    @GET("/3/movie/{sort_by}")
    suspend fun getMovies(
        @Path("sort_by") sortBy: String,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_KEY
    ): BaseApiResponse<Movie>

    @GET("/3/movie/{id}/reviews")
    fun getMovieReviews(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_KEY
    ): ApiResponse<Review>

    @GET("/3/movie/{id}/videos")
    fun getMovieTrailers(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_KEY
    ): ApiResponse<Trailer>
}