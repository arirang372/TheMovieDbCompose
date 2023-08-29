package com.john.themoviedb.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.john.themoviedb.AppViewModelProvider
import com.john.themoviedb.R
import com.john.themoviedb.models.Movie
import com.john.themoviedb.ui.TheMovieDbTopAppBar
import com.john.themoviedb.ui.navigation.NavigationDestination

object MovieDashboardDestination : NavigationDestination {
    override val route: String = "dashboard"
    override val titleRes = R.string.app_name
}

@Composable
fun MovieDashboardScreen(
    navigateToDetailPage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dashboardState by viewModel.dashboardState.collectAsState()
    when (dashboardState.uiState) {
        is MovieDashboardUiState.Loading -> LoadingScreen(
            modifier = modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is MovieDashboardUiState.Success -> MovieDashboardSuccessScreen(
            dashboardState = dashboardState,
            movieItemPressed = navigateToDetailPage,
            modifier = modifier
        )

        is MovieDashboardUiState.Error ->
            ErrorScreen(error = (dashboardState.uiState as MovieDashboardUiState.Error).message)

    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDashboardSuccessScreen(
    dashboardState: MovieDashboardState,
    movieItemPressed: (String) -> Unit,
    modifier: Modifier
) {
    Scaffold(topBar = {
        TheMovieDbTopAppBar(
            title =
            stringResource(id = MovieDashboardDestination.titleRes),
            canNavigateBack = false
        )
    })
    { innerPadding ->
        MovieDashboardContent(
            dashboardState = dashboardState,
            movieItemPressed = movieItemPressed,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun MovieDashboardContent(
    dashboardState: MovieDashboardState,
    movieItemPressed: (String) -> Unit,
    modifier: Modifier
) {
    val uiState = dashboardState.uiState as MovieDashboardUiState.Success
    val lazyPagingItems: LazyPagingItems<Movie> = uiState.movies.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns =  GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        if(lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                LoadingScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .size(200.dp)
                )
            }
        }

        items(
            count = lazyPagingItems.itemCount
        ) { index ->
            val movie = lazyPagingItems[index]!!
            MovieListItem(
                movie = movie,
                onMovieClick = {},
                modifier = modifier
            )
        }

        if(lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                LoadingScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .size(200.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListItem(
    movie: Movie,
    onMovieClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            ,
        onClick = onMovieClick,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(movie.poster_path)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.progress_bar),
            contentDescription = stringResource(id = R.string.movie_list_item_image),
            contentScale = ContentScale.FillBounds
        )
    }
}