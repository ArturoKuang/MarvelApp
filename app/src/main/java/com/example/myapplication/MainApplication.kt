package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        realm.close()
    }
}