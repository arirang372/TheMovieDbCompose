package com.john.themoviedb.data

import com.john.themoviedb.data.local.LocalDataSource
import com.john.themoviedb.data.remote.RemoteDataSource
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow


class DefaultMovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    override suspend fun deleteMovie(id: Long) = localDataSource.deleteMovie(id)

    override suspend fun getMovie(id: Long): Flow<Movie> =
        localDataSource.getMovie(id)

    override suspend fun loadAllMovies(sortBy: String) = remoteDataSource.loadAllMovies(sortBy)

    override fun loadReviewsAndTrailers(movieId: Long) =
        remoteDataSource.loadReviewsAndTrailers(movieId)

    override suspend fun saveMovie(movie: Movie) = localDataSource.saveMovie(movie)

}