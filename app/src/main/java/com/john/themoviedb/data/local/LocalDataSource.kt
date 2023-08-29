package com.john.themoviedb.data.local

import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllMovies(): Flow<List<Movie>>

    fun getMovie(id: Long): Flow<Movie>

    suspend fun saveMovie(movie: Movie)

    suspend fun deleteMovie(id: Long)

}