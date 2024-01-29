package com.john.themoviedb.ui.trailer

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.john.themoviedb.AppViewModelProvider
import com.john.themoviedb.R
import com.john.themoviedb.models.MovieDetails
import com.john.themoviedb.models.Trailer
import com.john.themoviedb.ui.navigation.NavigationDestination
import com.john.themoviedb.ui.provideExoPlayer

object MovieTrailerDestination : NavigationDestination {
    override val route: String = "trailer"
    override val titleRes = R.string.movie_trailer_title
    const val detailObj = "detailObj"
    val routeWithArgs = "$route?$detailObj={$detailObj}"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MovieTrailerScreen(
    navigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: MovieTrailerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = movieDetails.selectedTrailer.name.orEmpty()
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        )
    }) { padding ->
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                item {
                    MovieTrailerTopContent(
                        selectedMovieTrailer = movieDetails.selectedTrailer,
                        modifier = modifier
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }

                items(count = movieDetails.trailers.size) { index ->
                    MovieTrailerListItem(
                        trailer = movieDetails.trailers[index],
                        modifier = modifier,
                        onTrailerItemClicked = {
                            viewModel.updateSelectedTrailer(it)
                        }
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieTrailerListItem(
    trailer: Trailer,
    modifier: Modifier,
    onTrailerItemClicked: (Trailer) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = modifier) {
            AsyncImage(
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .padding(start = 16.dp, top = 2.dp),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(trailer.trailerImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.progress_bar),
                contentDescription = stringResource(id = R.string.movie_list_item_image),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(id = R.drawable.ic_play_trailer_holo_dark),
                contentDescription = null,
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp)
                    .align(Alignment.Center)
                    .clickable {
                        onTrailerItemClicked(trailer)
                    },
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = trailer.name.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun MovieTrailerTopContent(
    selectedMovieTrailer: Trailer,
    modifier: Modifier = Modifier
) {

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(275.dp),
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(
                    selectedMovieTrailer.trailerVideoUrl.orEmpty()
                )
            }
        },
        /**
         * Re-compose AndroidView when selectedMovieTrailer has changed ...
         * The details are explained below
         * https://developer.android.com/jetpack/compose/migrate/interoperability-apis/views-in-compose#:~:text=The%20AndroidView%20recomposes%20whenever%20a,position%20in%20the%20parent%20composable.
         */
        update = {
            it.loadUrl(
                selectedMovieTrailer.trailerVideoUrl.orEmpty()
            )
        }
    )
//    val playWhenReady by remember { mutableStateOf(true) }
//    val context = LocalContext.current
//    val uri = Uri.parse(movieDetails.selectedTrailer.trailerVideoUrl.orEmpty())
//    val mediaItem = MediaItem.fromUri(uri)
//    val playerView = PlayerView(context)
//    val player = provideExoPlayer(context = context, mediaItem = mediaItem)
//    playerView.player = player
//    LaunchedEffect(player) {
//        player.prepare()
//        player.playWhenReady = playWhenReady
//    }
//
//    AndroidView(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(300.dp),
//        factory = {
//            playerView
//        }
//    )
}