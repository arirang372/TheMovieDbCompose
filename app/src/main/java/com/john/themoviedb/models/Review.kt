package com.john.themoviedb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Review(
    var author: String?,
    var content: String?,
    var id: String?,
    var url: String?,
    var movieId: Long
) : Comparable<Review>, Parcelable {
    override fun compareTo(other: Review) = 0

    companion object {
        val EMPTY = Review(
            author = "",
            content = "",
            id = "",
            url = "",
            movieId = 0L
        )
    }
}

