package com.john.themoviedb.data.remote

import com.john.themoviedb.models.Category
import com.john.themoviedb.models.Movie
import com.john.themoviedb.models.Review
import com.john.themoviedb.models.Trailer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class MovieRemoteDataSource(
    private val service: MovieService
) : RemoteDataSource {

    override suspend fun loadAllMovies(sortBy: String): List<Movie> =
        service.getMovies(sortBy).results.map { movie ->
            createImageUrls(movie)
        }

    override fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>> {
        val trailers = flow {
            emit(
                service.getMovieTrailers(
                    movieId
                ).results
            )
        }
        val reviews = flow {
            emit(
                service.getMovieReviews(
                    movieId
                ).results
            )
        }

        return trailers.zip(reviews) { trailers, reviews ->
            var combinedList = mutableListOf<Comparable<*>>()
            combinedList.add(Category("Video"))
            combinedList.addAll(createTrailerUrls(trailers, movieId))
            combinedList.add(Category("Reviews"))
            combinedList.addAll(appendMovieIdsOnReviews(reviews, movieId))
            combinedList
        }.flowOn(Dispatchers.Default)
    }

    private fun appendMovieIdsOnReviews(
        reviews: MutableList<Review>,
        movieId: Long
    ): MutableList<Review> {
        for (review in reviews) {
            review.movieId = movieId
        }
        return reviews
    }

    private fun createTrailerUrls(
        trailers: MutableList<Trailer>,
        movieId: Long
    ): MutableList<Trailer> {
        for (trailer in trailers) {
            trailer.movieId = movieId
            trailer.trailerImageUrl = String.format(YOUTUBE_IMAGE_URL_BASE, trailer.key)
            trailer.trailerVideoUrl = String.format(YOUTUBE_VIDEO_URL_BASE, trailer.key)
        }
        return trailers
    }

    private fun getConvertedReleaseDate(movieReleaseDate: String?): String {
        var simpleDateFormat = SimpleDateFormat("yyyy-dd-MM", Locale.US)
        return movieReleaseDate.let {
            try {
                var date = simpleDateFormat.parse(it)
                DateFormat.getDateInstance().format(date)
            } catch (e: ParseException) { //do nothing. }
                "null release date"
            }
        }
    }

    private fun createImageUrls(m: Movie): Movie {
        m.poster_path = String.format(POSTER_IMAGE_URL_BASE, m.poster_path)
        m.backdrop_path = String.format(BACK_DROP_IMAGE_URL_BASE, m.backdrop_path)
        m.release_date = getConvertedReleaseDate(m.release_date)
        m.rating = m.vote_average?.let { it.toFloat() }!!
        m.rating = m.rating / 2
        return m
    }

    companion object {
        private const val POSTER_IMAGE_URL_BASE = "http://image.tmdb.org/t/p/w342%s"
        private const val BACK_DROP_IMAGE_URL_BASE = "http://image.tmdb.org/t/p/original%s"
        private const val YOUTUBE_VIDEO_URL_BASE = "http://www.youtube.com/watch?v=%s"
        private const val YOUTUBE_IMAGE_URL_BASE = "http://img.youtube.com/vi/%s/0.jpg"
    }
}