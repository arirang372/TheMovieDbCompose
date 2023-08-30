package com.john.themoviedb.ui.dashboard

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.Flow

data class MovieDashboardState(
    val movies: List<Movie> = emptyList(),
    val isShowingHomepage: Boolean = true,
    val uiState: MovieDashboardUiState = MovieDashboardUiState.Loading
)

sealed interface MovieDashboardUiState {
    data class Success(val movies: Flow<PagingData<Movie>>) : MovieDashboardUiState
    data class Error(val message: String) : MovieDashboardUiState
    object Loading : MovieDashboardUiState
}

data class DropDownMenuItemModel(
    val onSearchMoviesByType: () -> Unit,
    @StringRes val menuTextResource: Int
)

enum class MovieType(val type: String) {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    FAVORITE("favorite")
}