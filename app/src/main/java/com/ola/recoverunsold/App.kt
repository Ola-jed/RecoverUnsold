package com.ola.recoverunsold

import android.app.Application
import com.ola.recoverunsold.di.appModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}