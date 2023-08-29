package com.john.themoviedb.data.remote.models

import com.google.gson.annotations.SerializedName

class ApiResponse<T : Comparable<T>> : BaseApiResponse<T>() {
    @SerializedName("id")
    var id: Int = 0
}