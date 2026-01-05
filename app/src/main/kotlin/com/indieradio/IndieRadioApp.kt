package com.indieradio

import android.app.Application
import com.indieradio.util.CrashLogger
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class with Hilt support
 */
@HiltAndroidApp
class IndieRadioApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize crash logger to capture crashes
        CrashLogger(this)
    }
}
