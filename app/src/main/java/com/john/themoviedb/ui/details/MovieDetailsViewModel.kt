package com.john.themoviedb.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.john.themoviedb.data.MovieRepository
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MovieDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository) : ViewModel() {

    private val _detailState = MutableStateFlow(MovieDetailsState())
    val detailState: StateFlow<MovieDetailsState> = _detailState


    init {
        val movieString: String = checkNotNull(savedStateHandle[MovieDetailsDestination.movieObj])
        val selectedMovie: Movie = Gson().fromJson(movieString, Movie::class.java)
        _detailState.value = _detailState.value.copy(
            movie = selectedMovie,
            uiState = MovieDetailsUiState.Success(emptyList())
        )
    }


    fun fetchMovieTrailersAndReviews(movieId: Long) = viewModelScope.launch {

        try {
            repository.loadReviewsAndTrailers(movieId)
                .collect { trailersAndReviews ->
                    _detailState.value =
                        _detailState.value.copy(
                            uiState = MovieDetailsUiState.Success(trailersAndReviews)
                        )
                }
        } catch (exception: Exception) {
            _detailState.value =
                _detailState.value.copy(
                    uiState = MovieDetailsUiState.Error(exception.message.toString())
                )
        }
    }
}