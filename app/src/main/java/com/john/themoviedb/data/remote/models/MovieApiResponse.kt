package com.john.themoviedb.data.remote.models

import com.google.gson.annotations.SerializedName
import com.john.themoviedb.models.Movie

data class MovieApiResponse(
    @field:SerializedName("results") var results: MutableList<Movie> = mutableListOf(),
    @field:SerializedName("total_pages") var totalPages: Int = 0,
    @field:SerializedName("total_results") var totalResults: Int = 0,
    @field:SerializedName("page") var page: Int = 0,
)