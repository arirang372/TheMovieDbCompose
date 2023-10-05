package com.john.themoviedb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.john.themoviedb.ui.dashboard.MovieDashboardDestination
import com.john.themoviedb.ui.dashboard.MovieDashboardScreen
import com.john.themoviedb.ui.details.MovieDetailsDestination
import com.john.themoviedb.ui.details.MovieDetailsScreen


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
            MovieDashboardScreen(navigateToDetailPage = { movie ->
                navController.navigate("${MovieDetailsDestination.route}?${MovieDetailsDestination.movieObj}=${movie}")
            })
        }
        composable(
            route = MovieDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(MovieDetailsDestination.movieObj) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            MovieDetailsScreen(navigateBack = {
                navController.navigateUp()
            }, modifier = modifier)
        }
    }
}