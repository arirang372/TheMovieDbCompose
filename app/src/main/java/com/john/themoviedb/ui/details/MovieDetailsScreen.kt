package com.john.themoviedb.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.john.themoviedb.AppViewModelProvider
import com.john.themoviedb.COLLAPSED_TOP_BAR_HEIGHT
import com.john.themoviedb.EXPANDED_TOP_BAR_HEIGHT
import com.john.themoviedb.R
import com.john.themoviedb.models.Category
import com.john.themoviedb.models.Movie
import com.john.themoviedb.models.MovieDetails
import com.john.themoviedb.models.Review
import com.john.themoviedb.models.Trailer
import com.john.themoviedb.ui.ErrorScreen
import com.john.themoviedb.ui.LoadingScreen
import com.john.themoviedb.ui.navigation.NavigationDestination
import com.john.themoviedb.ui.ratingbar.RatingBar
import com.john.themoviedb.ui.ratingbar.RatingBarStyle


object MovieDetailsDestination : NavigationDestination {
    override val route: String = "details"
    override val titleRes = R.string.app_name
    const val movieObj = "movieObject"
    val routeWithArgs = "$route?$movieObj={$movieObj}"
}

@Composable
fun MovieDetailsScreen(
    navigateBack: () -> Unit,
    navigateToMovieTrailerPage: (String) -> Unit,
    modifier: Modifier,
    viewModel: MovieDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val detailsState by viewModel.detailState.collectAsState()
    when (detailsState.uiState) {
        is MovieDetailsUiState.Loading -> LoadingScreen(
            modifier = modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is MovieDetailsUiState.Success -> MovieDetailsContentScreen(
            viewModel = viewModel,
            detailsState = detailsState,
            navigateToMovieTrailerPage,
            modifier = modifier,
            onBackPressed = navigateBack
        )

        is MovieDetailsUiState.Error ->
            ErrorScreen(error = (detailsState.uiState as MovieDetailsUiState.Error).message)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MovieDetailsContentScreen(
    viewModel: MovieDetailsViewModel,
    detailsState: MovieDetailsState,
    trailerPressed: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {

    val listState = rememberLazyListState()
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Scaffold(
        topBar = {
            CollapsedTopAppBar(
                movie = detailsState.movie,
                navigateBack = onBackPressed,
                modifier = modifier,
                isCollapsed = isCollapsed
            )
        }
    ) { padding ->
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyColumn(
                modifier = Modifier.padding(padding),
                state = listState
            ) {
                item {
                    ExpandedTopBar(
                        movie = detailsState.movie,
                        navigateBack = onBackPressed,
                        modifier
                    )
                }
                item {
                    MovieDetailsTopContent(
                        viewModel,
                        detailsState.movie,
                        modifier
                    )
                }

                val trailers = detailsState.trailersAndReviews.filterIsInstance<Trailer>()

                items(count = detailsState.trailersAndReviews.size) { index ->
                    MovieDetailsBottomEachContent(
                        detailsState.trailersAndReviews[index],
                        trailers = trailers,
                        trailerPressed = trailerPressed,
                        modifier
                    )
                }
            }
        }
    }
}

private fun openUri(context: Context, uri: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}


@Composable
private fun MovieDetailsTopContent(
    viewModel: MovieDetailsViewModel,
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(start = 8.dp, top = 8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(movie.poster_path)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.movie_poster_item_image),
                modifier = modifier
                    .width(140.dp)
                    .height(210.dp)
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                )

                RatingBar(
                    value = movie.rating,
                    spaceBetween = 0.dp,
                    style = RatingBarStyle.Fill(),
                )
                val buttonText = viewModel.getButtonText()
                Button(
                    onClick = {
                        if (buttonText == R.string.mark_as_favorite) {
                            viewModel.markMovieAsFavorite(movie = movie)
                        } else {
                            viewModel.removeMovieFromFavorite(movie)
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(id = buttonText))
                }

                Text(
                    text = stringResource(
                        id = R.string.vote_average_text,
                        movie.vote_average.orEmpty()
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.padding(top = 8.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.release_date_text,
                        movie.release_date.orEmpty()
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Text(
            text = movie.overview.orEmpty(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun MovieDetailsBottomEachContent(
    content: Comparable<*>,
    trailers: List<Trailer>,
    trailerPressed: (String)-> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    when (content) {
        is Category -> {
            TitleListItem(content, modifier = modifier)
        }

        is Trailer -> {
            TrailerListItem(content, { content ->
                //openUri(context, content.trailerVideoUrl.orEmpty())
                val movieDetails = MovieDetails(
                    trailers = trailers,
                    selectedTrailer = content
                )
                val json = Gson().toJson(movieDetails)
                trailerPressed(json)
            }, modifier = modifier)
        }

        is Review -> {
            ReviewListItem(content, { content ->
                openUri(context, content.url.orEmpty())
            }, modifier = modifier)
        }
    }
}

@Composable
private fun CollapsedTopAppBar(
    movie: Movie,
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    isCollapsed: Boolean
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
        label = ""
    )
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .height(COLLAPSED_TOP_BAR_HEIGHT)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(visible = isCollapsed) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigateBack,
                    modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                Text(text = movie.title.orEmpty())
            }
        }
    }
}


@Composable
private fun ExpandedTopBar(
    movie: Movie,
    navigateBack: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(EXPANDED_TOP_BAR_HEIGHT - COLLAPSED_TOP_BAR_HEIGHT),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(movie.backdrop_path)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.progress_bar),
            contentDescription = stringResource(id = R.string.movie_list_item_image),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.White
                )
            }
            Text(
                modifier = modifier.padding(start = 16.dp, bottom = 16.dp),
                text = movie.title.orEmpty(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

}