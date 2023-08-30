package com.john.themoviedb.ui


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.john.themoviedb.ui.navigation.TheMovieDbNavHost

@Composable
fun TheMovieDbApp(navController: NavHostController = rememberNavController()) {
    TheMovieDbNavHost(navController = navController)
}

