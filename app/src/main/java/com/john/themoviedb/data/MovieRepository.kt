package com.john.themoviedb.data

import androidx.paging.PagingData
import com.john.themoviedb.data.remote.models.ApiResponse
import com.john.themoviedb.models.Movie
import com.john.themoviedb.models.Review
import com.john.themoviedb.models.Trailer
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun loadAllMovies(sortBy: String): Flow<PagingData<Movie>>

    suspend fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>>

    suspend fun getMovie(id: Long): Flow<Movie>

    suspend fun saveMovie(movie: Movie)

    suspend fun deleteMovie(id: Long)

}