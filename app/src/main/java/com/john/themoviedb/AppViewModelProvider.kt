package com.john.themoviedb

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.john.themoviedb.ui.dashboard.MovieDashboardViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MovieDashboardViewModel(
                theMovieDbApplication().container.repository
            )
        }
    }

}


fun CreationExtras.theMovieDbApplication(): TheMovieDbApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheMovieDbApplication