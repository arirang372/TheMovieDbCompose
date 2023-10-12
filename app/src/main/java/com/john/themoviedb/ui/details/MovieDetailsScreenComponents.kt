package com.john.themoviedb.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.john.themoviedb.R
import com.john.themoviedb.models.Category
import com.john.themoviedb.models.Review
import com.john.themoviedb.models.Trailer


@Composable
fun TitleListItem(
    category: Category,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {

        Divider()

        Text(
            text = category.categoryType.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}


@Composable
fun TrailerListItem(
    trailer: Trailer,
    onTrailerPressed: (Trailer) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = modifier) {
            AsyncImage(
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .padding(start = 16.dp, top = 2.dp)
                ,
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(trailer.trailerImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.progress_bar),
                contentDescription = stringResource(id = R.string.movie_trailer_item_image),
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
                        onTrailerPressed(trailer)
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

@Composable
fun ReviewListItem(
    review: Review,
    onReviewPressed: (Review) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .clickable {
                onReviewPressed(review)
            }
    ) {

        Text(
            text = review.author.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Blue
        )

        Text(
            text = review.content.orEmpty(),
            modifier = modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}