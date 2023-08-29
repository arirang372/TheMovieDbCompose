package com.john.themoviedb.ui.dashboard

import com.john.themoviedb.models.Movie

data class MovieDashboardState(
    val movies: List<Movie> = emptyList(),
    val isShowingHomepage: Boolean = true,
    val uiState: MovieDashboardUiState = MovieDashboardUiState.Loading
)


sealed interface MovieDashboardUiState {

    data class Success(val movies: List<Movie>) : MovieDashboardUiState

    data class Error(val message: String) : MovieDashboardUiState

    object Loading : MovieDashboardUiState

}


enum class MovieType(val type: String) {
    POPULAR("popular"), TOP_RATED("top_rated"), FAVORITE("favorite")
}