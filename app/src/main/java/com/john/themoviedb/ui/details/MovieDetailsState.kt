package com.john.themoviedb.ui.details

import com.john.themoviedb.models.Movie


data class MovieDetailsState(
    val movie: Movie = Movie(
        id = 0,
        vote_average = null,
        rating = 0f,
        poster_path = null,
        overview = null,
        title = null,
        release_date = null,
        backdrop_path = null
    ),
    val trailersAndReviews: List<Comparable<*>> = emptyList(),
    val isFavorite: Boolean = false,
    val uiState: MovieDetailsUiState = MovieDetailsUiState.Loading
)

sealed interface MovieDetailsUiState {

    data class Success(val trailersAndReviews: List<Comparable<*>>) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState

    object Loading : MovieDetailsUiState

}