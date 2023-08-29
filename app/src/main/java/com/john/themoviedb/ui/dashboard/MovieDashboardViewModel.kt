package com.john.themoviedb.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.themoviedb.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDashboardViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _dashboardState = MutableStateFlow(MovieDashboardState())
    val dashboardState: StateFlow<MovieDashboardState> = _dashboardState

    private val _sortByField = MutableStateFlow(MovieType.POPULAR)
    val sortByField: StateFlow<MovieType> = _sortByField

    init {
        val movieType: MovieType = sortByField.value
        fetchMovies(movieType.type)
    }

    fun fetchMovies(sortBy: String) = viewModelScope.launch {
        try {
            val results = repository.loadAllMovies(sortBy)
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Success(results)
                )
        } catch (exception: Exception) {
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Error(exception.message.toString())
                )
        }
    }
}