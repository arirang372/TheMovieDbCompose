package com.john.themoviedb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer(
    var id: String?,
    var key: String?,
    var name: String?,
    var site: String?,
    var size: Int,
    var type: String?,
    var trailerImageUrl: String?,
    var trailerVideoUrl: String?,
    var movieId: Long
) : Comparable<Trailer>, Parcelable {
    override fun compareTo(other: Trailer) = 0

    companion object {
        val EMPTY = Trailer(
            id = "",
            key = "",
            name = "",
            site = "",
            size = 0,
            type = "",
            trailerImageUrl = "",
            trailerVideoUrl = "",
            movieId = 0L
        )
    }
}