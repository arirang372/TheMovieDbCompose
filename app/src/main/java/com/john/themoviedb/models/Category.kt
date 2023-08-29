package com.john.themoviedb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(var categoryType: String?) : Comparable<Category>, Parcelable {
    override fun compareTo(other: Category) = 0
}


