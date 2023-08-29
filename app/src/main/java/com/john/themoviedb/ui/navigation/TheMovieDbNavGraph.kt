package com.john.themoviedb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.john.themoviedb.ui.dashboard.MovieDashboardDestination
import com.john.themoviedb.ui.dashboard.MovieDashboardScreen


@Composable
fun TheMovieDbNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MovieDashboardDestination.route,
        modifier = modifier
    ) {
        composable(
            route = MovieDashboardDestination.route
        ) {
            MovieDashboardScreen(navigateToDetailPage = {})
        }
    }
}