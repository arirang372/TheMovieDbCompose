package com.john.themoviedb

import android.app.Application
import com.john.themoviedb.data.AppContainer
import com.john.themoviedb.data.AppDataContainer

class TheMovieDbApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}