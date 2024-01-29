package com.john.themoviedb.ui.details

import com.john.themoviedb.models.Movie


data class MovieDetailsState(
    val movie: Movie = Movie.EMPTY,
    val trailersAndReviews: List<Comparable<*>> = emptyList(),
    val isFavorite: Boolean = false,
    val uiState: MovieDetailsUiState = MovieDetailsUiState.Loading
)

sealed interface MovieDetailsUiState {

    data class Success(val trailersAndReviews: List<Comparable<*>>) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState

    object Loading : MovieDetailsUiState

}