package com.john.themoviedb.data.remote

import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun loadAllMovies(sortBy: String): List<Movie>

    fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>>

}