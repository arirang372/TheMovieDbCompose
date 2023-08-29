package com.john.themoviedb.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.themoviedb.data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.subscribeOn
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
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Success(repository.loadAllMovies(sortBy))
                )

        } catch (exception: Exception) {
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = MovieDashboardUiState.Error(exception.message.toString())
                )
        }
    }
}