package com.john.themoviedb.ui.dashboard


import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.themoviedb.R
import com.john.themoviedb.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDashboardViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _dashboardState = MutableStateFlow(MovieDashboardState())
    val dashboardState: StateFlow<MovieDashboardState> = _dashboardState

    private val _sortByFieldStringRes = MutableStateFlow(R.string.popular_movie)
    val sortByFieldStringRes: StateFlow<Int> = _sortByFieldStringRes

    init {
        fetchMovies(MovieType.POPULAR.type)
    }

    private fun updateSortByFieldStringResource(@StringRes movieTypeStringRes: Int) {
        _sortByFieldStringRes.value = movieTypeStringRes
    }

    fun createDropDownMenuItemModels() = listOf(
        DropDownMenuItemModel(
            onSearchMoviesByType = {
                updateSortByFieldStringResource(R.string.popular_movie)
                fetchMovies(MovieType.POPULAR.type)
            },
            menuTextResource = R.string.popular_movie
        ),
        DropDownMenuItemModel(
            onSearchMoviesByType = {
                updateSortByFieldStringResource(R.string.top_rated_movie)
                fetchMovies(MovieType.TOP_RATED.type)
            },
            menuTextResource = R.string.top_rated_movie
        ),
        DropDownMenuItemModel(
            onSearchMoviesByType = {
                updateSortByFieldStringResource(R.string.now_playing_movie)
                fetchMovies(MovieType.NOW_PLAYING.type)
            },
            menuTextResource = R.string.now_playing_movie
        ),
        DropDownMenuItemModel(
            onSearchMoviesByType = {
                updateSortByFieldStringResource(R.string.upcoming_movie)
                fetchMovies(MovieType.UPCOMING.type)
            },
            menuTextResource = R.string.upcoming_movie
        )
    )

    private fun fetchMovies(sortBy: String) = viewModelScope.launch {
        try {
            val moviesPagingData = repository.loadAllMovies(sortBy)
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Success(moviesPagingData)
                )

        } catch (exception: Exception) {
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Error(exception.message.toString())
                )
        }
    }
}