package com.john.themoviedb.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.john.themoviedb.ui.theme.TheMovieDBComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheMovieDBComposeTheme {
                TheMovieDbApp()
            }
        }
    }
}

fun provideExoPlayer(context: Context, mediaItem: MediaItem): ExoPlayer =
    ExoPlayer.Builder(context)
        .build().apply {
            this.setMediaItem(mediaItem)
        }

