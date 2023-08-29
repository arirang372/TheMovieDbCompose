package com.john.themoviedb.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.john.themoviedb.models.Movie
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

private const val STARTING_PAGE_INDEX = 1

/**
 *  PagingSource that fetches the movies by page
 *
 */
internal class MoviesPagingSource(
    private val service: MovieService,
    private val sortBy: String
) : PagingSource<Int, Movie>() {

    private fun createImageUrls(m: Movie): Movie {
        m.poster_path = String.format(POSTER_IMAGE_URL_BASE, m.poster_path)
        m.backdrop_path = String.format(BACK_DROP_IMAGE_URL_BASE, m.backdrop_path)
        m.release_date = getConvertedReleaseDate(m.release_date)
        m.rating = m.vote_average?.let { it.toFloat() }!!
        m.rating = m.rating / 2
        return m
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: STARTING_PAGE_INDEX
            val searchResponse = service.getMovies(sortBy, nextPage)
            val results = searchResponse.results.map { movie ->
                createImageUrls(movie)
            }
            LoadResult.Page(
                data = results,
                prevKey = if (nextPage == STARTING_PAGE_INDEX) null else nextPage - 1,
                nextKey = searchResponse.page.plus(1)
            )

        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val POSTER_IMAGE_URL_BASE = "http://image.tmdb.org/t/p/w342%s"
        private const val BACK_DROP_IMAGE_URL_BASE = "http://image.tmdb.org/t/p/original%s"
    }
}