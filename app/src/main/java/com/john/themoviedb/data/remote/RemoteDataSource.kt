package com.john.themoviedb.data.remote

import androidx.paging.PagingData
import com.john.themoviedb.data.remote.models.ApiResponse
import com.john.themoviedb.models.Movie
import com.john.themoviedb.models.Trailer
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun loadAllMoviesPagingData(sortBy: String): Flow<PagingData<Movie>>

    suspend fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>>
}