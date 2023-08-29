package com.john.themoviedb.data.local

import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow


class MovieLocalDataSource(private val dao: MovieDao) : LocalDataSource {

    override fun getAllMovies(): Flow<List<Movie>> = dao.getAllMovies()

    override fun getMovie(id: Long): Flow<Movie> = dao.getMovieById(id)

    override suspend fun saveMovie(movie: Movie) = dao.upsert(movie = movie)

    override suspend fun deleteMovie(id: Long) = dao.deleteMovieById(id = id)
}