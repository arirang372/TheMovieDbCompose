package com.john.themoviedb.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.john.themoviedb.data.remote.models.ApiResponse
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

    override fun loadAllMoviesPagingData(sortBy: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            pagingSourceFactory = {
                MoviesPagingSource(service, sortBy)
            }
        ).flow
    }

    override suspend fun loadReviewsAndTrailers(movieId: Long): Flow<List<Comparable<*>>> {
        val trailers: Flow<List<Trailer>> = flow {
            emit(
                service.getMovieTrailers(
                    movieId
                ).results
            )
        }
        val reviews: Flow<List<Review>> = flow {
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
        reviews: List<Review>,
        movieId: Long
    ): List<Review> {
        for (review in reviews) {
            review.movieId = movieId
        }
        return reviews
    }

    private fun createTrailerUrls(
        trailers: List<Trailer>,
        movieId: Long
    ): List<Trailer> {
        for (trailer in trailers) {
            trailer.movieId = movieId
            trailer.trailerImageUrl = String.format(YOUTUBE_IMAGE_URL_BASE, trailer.key)
            trailer.trailerVideoUrl = String.format(YOUTUBE_VIDEO_URL_BASE, trailer.key)
        }
        return trailers
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 5
        private const val YOUTUBE_VIDEO_URL_BASE = "http://www.youtube.com/watch?v=%s"
        private const val YOUTUBE_IMAGE_URL_BASE = "http://img.youtube.com/vi/%s/0.jpg"
    }
}