package com.flysolo.e_appoint

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppointmentApp  : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}