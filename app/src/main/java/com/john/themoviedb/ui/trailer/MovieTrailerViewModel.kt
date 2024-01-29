package com.john.themoviedb.ui.trailer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.john.themoviedb.models.MovieDetails
import com.john.themoviedb.models.Trailer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class MovieTrailerViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetails = MutableStateFlow(MovieDetails())
    val movieDetails: StateFlow<MovieDetails> = _movieDetails

    init {
        val movieDetailsString: String =
            checkNotNull(savedStateHandle[MovieTrailerDestination.detailObj])
        val movieDetails: MovieDetails =
            Gson().fromJson(movieDetailsString, MovieDetails::class.java)

        _movieDetails.value = movieDetails
    }

    fun updateSelectedTrailer(selectedTrailer: Trailer) {
        _movieDetails.update {
            it.copy(
                selectedTrailer = selectedTrailer
            )
        }
    }
}