package com.john.themoviedb.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheMovieDbTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        TopAppBar(
            title = {
                Text(title)
            },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            modifier = modifier.fillMaxWidth()
        )
    } else {
        TopAppBar(
            title = {
                Text(title)
            },
            actions = {
                FilterMovieTypesMenu()
            },
            modifier = modifier
        )
    }
}

@Composable
private fun FilterMovieTypesMenu() {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_filter_list),
                stringResource(id = R.string.menu_more)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(onClick = { closeMenu() }, text = {
            Text(text = stringResource(id = R.string.popular_movie))
        })
        DropdownMenuItem(onClick = { closeMenu() }, text = {
            Text(text = stringResource(id = R.string.top_rated_movie))
        })
        DropdownMenuItem(onClick = { closeMenu() }, text = {
            Text(text = stringResource(id = R.string.now_playing_movie))
        })
        DropdownMenuItem(onClick = { closeMenu() }, text = {
            Text(text = stringResource(id = R.string.upcoming_movie))
        })
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}

