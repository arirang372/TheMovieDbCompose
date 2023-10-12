package com.john.themoviedb.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.john.themoviedb.data.MovieRepository
import com.john.themoviedb.models.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.john.themoviedb.R


class MovieDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow(MovieDetailsState())
    val detailState: StateFlow<MovieDetailsState> = _detailState

    init {
        val movieString: String = checkNotNull(savedStateHandle[MovieDetailsDestination.movieObj])
        val selectedMovie: Movie = Gson().fromJson(movieString, Movie::class.java)
        _detailState.value = _detailState.value.copy(
            movie = selectedMovie
        )
        fetchMovieTrailersAndReviews(selectedMovie.id)
        setMovieFavorite(selectedMovie.id)
    }

    fun markMovieAsFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.saveMovie(movie)
            setMovieFavorite(movie.id)
        }
    }

    fun removeMovieFromFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie.id)
            setMovieFavorite(movie.id)
        }
    }

    private fun setMovieFavorite(id: Long) = viewModelScope.launch {
        repository.getMovie(id).collect { movie ->
            if (movie == null) {
                updateIsFavorite(false)
            } else {
                updateIsFavorite(true)
            }
        }
    }

    private fun updateIsFavorite(isFavorite: Boolean) {
        _detailState.update {
            it.copy(
                isFavorite = isFavorite
            )
        }
    }

    fun getButtonText() = if (_detailState.value.isFavorite) {
        R.string.remove_from_favorite
    } else {
        R.string.mark_as_favorite
    }


    private fun fetchMovieTrailersAndReviews(movieId: Long) = viewModelScope.launch {
        try {
            repository.loadReviewsAndTrailers(movieId)
                .collect { trailersAndReviews ->
                    _detailState.value =
                        _detailState.value.copy(
                            trailersAndReviews = trailersAndReviews,
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