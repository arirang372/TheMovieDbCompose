package com.john.themoviedb.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.john.themoviedb.R
import com.john.themoviedb.ui.navigation.TheMovieDbNavHost

@Composable
fun TheMovieDbApp(navController: NavHostController = rememberNavController()) {
    TheMovieDbNavHost(navController = navController)
}


@Composable
fun LoadingScreen(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.progress_bar),
        contentDescription = stringResource(id = R.string.loading),
        modifier = modifier
    )
}


@Composable
fun ErrorScreen(modifier: Modifier = Modifier, error: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error)
    }
}