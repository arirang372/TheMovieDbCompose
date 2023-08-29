package com.john.themoviedb.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,
    @ColumnInfo(name = "vote_average")
    var vote_average: String?,
    @ColumnInfo(name = "rating")
    var rating: Float,
    @ColumnInfo(name = "poster_path")
    var poster_path: String?,
    @ColumnInfo(name = "overview")
    var overview: String?,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "release_date")
    var release_date: String?,
    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String?
) : Comparable<Movie>, Parcelable {
    override fun compareTo(other: Movie) = 0
}

