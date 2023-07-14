package com.ola.recoverunsold.utils.store

import android.content.Context
import com.ola.recoverunsold.ui.theme.ThemeMode
import com.ola.recoverunsold.utils.enums.Locale

object AppPreferences {
    private const val SHARED_PREFERENCES = "sp"
    private const val THEME_MODE = "theme-mode"
    private const val LOCALE = "locale"

    fun getTheme(context: Context): ThemeMode {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val modeAsInt = sharedPrefs.getInt(THEME_MODE, ThemeMode.Auto.value)
        return ThemeMode.getByValue(modeAsInt)
    }

    fun updateTheme(context: Context, themeMode: ThemeMode) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt(THEME_MODE, themeMode.value)
            apply()
        }
    }

    fun getLocale(context: Context): Locale {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val localeCode = sharedPrefs.getString(LOCALE, Locale.Auto.code) ?: Locale.Auto.code
        return Locale.getByCode(localeCode)
    }

    fun updateLocale(context: Context, locale: Locale) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString(LOCALE, locale.code)
            apply()
        }
    }
}