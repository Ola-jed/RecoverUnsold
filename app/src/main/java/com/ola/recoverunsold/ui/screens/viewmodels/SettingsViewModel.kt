package com.ola.recoverunsold.ui.screens.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ola.recoverunsold.ui.theme.ThemeMode
import com.ola.recoverunsold.utils.misc.Locale
import com.ola.recoverunsold.utils.store.AppPreferences
import com.ola.recoverunsold.utils.store.LocaleObserver
import com.ola.recoverunsold.utils.store.ThemeObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    var showThemeDialog by mutableStateOf(false)
    var showLocaleDialog by mutableStateOf(false)
    var theme = ThemeObserver.themeMode.value
    var currentLocale = LocaleObserver.locale.value

    fun updateCurrentLocale(context: Context, locale: Locale) {
//        val phoneLocale = java.util.Locale(locale.code)
//        java.util.Locale.setDefault(phoneLocale)
//        context.resources
//        val config = Configuration()
//        config.setLocale(phoneLocale)
//        context.createConfigurationContext(config)
//        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        AppPreferences.updateLocale(context, locale)
        LocaleObserver.update(locale)
        currentLocale = locale
        context.startActivity(Intent.makeRestartActivityTask((context as Activity).intent?.component))
    }

    fun updateCurrentTheme(context: Context, themeMode: ThemeMode) {
        AppPreferences.updateTheme(context, themeMode)
        ThemeObserver.update(themeMode)
        theme = themeMode
    }
}