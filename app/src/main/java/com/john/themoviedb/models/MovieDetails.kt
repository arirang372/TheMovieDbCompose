package com.john.themoviedb.models

data class MovieDetails(
    val selectedTrailer: Trailer = Trailer.EMPTY,
    val trailers: List<Trailer> = emptyList(),
    val selectedReview: Review = Review.EMPTY,
    val reviews: List<Review> = emptyList()
)