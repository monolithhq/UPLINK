package com.uplink

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UplinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
