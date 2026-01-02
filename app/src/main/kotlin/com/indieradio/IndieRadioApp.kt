package com.indieradio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class with Hilt support
 */
@HiltAndroidApp
class IndieRadioApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Application initialization will go here
    }
}
