package com.john.themoviedb.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.john.themoviedb.R
import com.john.themoviedb.ui.navigation.TheMovieDbNavHost


@Composable
fun TheMovieDbApp(navController: NavHostController = rememberNavController()) {
    TheMovieDbNavHost(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheMovieDbTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        TopAppBar(title = {
            Text(title)
        }, modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        )
    } else {
        TopAppBar(
            title = {
                Text(title)
            },
            modifier = modifier
        )
    }
}