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
            BuildConfig.KKIAPAY_API_KEY,
            SdkConfig(
                themeColor = R.color.main_color,
                imageResource = R.raw.logo,
                enableSandbox = false
            )
        )
        instance = this
    }
}