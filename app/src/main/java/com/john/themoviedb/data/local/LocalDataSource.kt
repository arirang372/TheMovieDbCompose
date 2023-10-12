package com.john.themoviedb.data.local

import androidx.paging.PagingData
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllMovies(): Flow<PagingData<Movie>>

    fun getMovie(id: Long): Flow<Movie?>

    suspend fun saveMovie(movie: Movie)

    suspend fun deleteMovie(id: Long)

}