package com.ola.recoverunsold

import android.app.Application
import co.opensi.kkiapay.uikit.Kkiapay
import co.opensi.kkiapay.uikit.SdkConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        Kkiapay.init(
            applicationContext,
            "d96163be613b25aa2e82b3e7163116af14cf2f04",
            SdkConfig(
                themeColor = R.color.main_color,
                imageResource = R.raw.logo,
                enableSandbox = true
            )
        )
        instance = this
    }
}