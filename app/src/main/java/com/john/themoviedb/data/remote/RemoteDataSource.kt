package com.john.themoviedb.data.remote

import androidx.paging.PagingData
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun loadAllMoviesPagingData(sortBy: String): Flow<PagingData<Movie>>

    fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>>

}