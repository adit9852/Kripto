package com.camo.kripto

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.camo.kripto.utils.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class Kripto: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // Apply saved theme preference (supports light/dark/system)
        ThemeManager.applyTheme(this)
    }
}