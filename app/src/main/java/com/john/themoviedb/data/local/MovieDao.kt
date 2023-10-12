package com.john.themoviedb.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllMovies(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieById(id: Long): Flow<Movie?>

    @Upsert
    suspend fun upsert(movie: Movie)

    @Query("DELETE FROM Movie WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

}