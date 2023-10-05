package com.john.themoviedb.data.remote.models

import com.google.gson.annotations.SerializedName

open class BaseApiResponse<T : Comparable<T>> {
    @SerializedName("results")
    var results: List<T> = emptyList()

    @SerializedName("total_pages")
    var totalPages: Int = 0

    @SerializedName("total_results")
    var totalResults: Int = 0

    @SerializedName("page")
    var page: Int = 0
}