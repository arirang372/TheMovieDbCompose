package com.john.themoviedb.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.john.themoviedb.data.local.LocalDataSource
import com.john.themoviedb.data.local.MovieDatabase
import com.john.themoviedb.data.local.MovieLocalDataSource
import com.john.themoviedb.data.remote.MovieRemoteDataSource
import com.john.themoviedb.data.remote.MovieService
import com.john.themoviedb.data.remote.RemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val repository: MovieRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val localDataSource: LocalDataSource by lazy {
        MovieLocalDataSource(database.movieDao())
    }

    private val logger = HttpLoggingInterceptor { Log.d("API", it) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofitService: MovieService by lazy {
        retrofit.create(MovieService::class.java)
    }

    private val remoteDataSource: RemoteDataSource by lazy {
        MovieRemoteDataSource(retrofitService)
    }

    private val database: MovieDatabase by lazy {
        Room.databaseBuilder(
            context = context.applicationContext,
            MovieDatabase::class.java,
            "Movie.db"
        ).build()
    }

    override val repository: MovieRepository by lazy {
        DefaultMovieRepository(remoteDataSource, localDataSource)
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/"
    }
}