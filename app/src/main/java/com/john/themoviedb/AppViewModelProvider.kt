package com.john.themoviedb

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.john.themoviedb.ui.dashboard.MovieDashboardViewModel
import com.john.themoviedb.ui.details.MovieDetailsViewModel
import com.john.themoviedb.ui.trailer.MovieTrailerViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MovieDashboardViewModel(
                theMovieDbApplication().container.repository
            )
        }
        initializer {
            MovieDetailsViewModel(
                this.createSavedStateHandle(),
                theMovieDbApplication().container.repository
            )
        }
        initializer {
            MovieTrailerViewModel(
                this.createSavedStateHandle()
            )
        }
    }
}


fun CreationExtras.theMovieDbApplication(): TheMovieDbApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheMovieDbApplication