package com.ola.recoverunsold.ui.screens.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ola.recoverunsold.ui.theme.THEME_MODE
import com.ola.recoverunsold.ui.theme.THEME_PREFERENCES
import com.ola.recoverunsold.ui.theme.ThemeMode
import com.ola.recoverunsold.utils.store.ThemeObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    var showThemeDialog by mutableStateOf(false)
    var theme = ThemeObserver.themeMode.value

    fun updateCurrentTheme(context: Context, themeMode: ThemeMode) {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt(THEME_MODE, themeMode.value)
            apply()
        }
        ThemeObserver.update(themeMode)
        theme = themeMode
    }
}