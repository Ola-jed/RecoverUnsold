package com.ola.recoverunsold.utils.store

import com.ola.recoverunsold.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeObserver {
    private val themeModeFlow = MutableStateFlow<ThemeMode?>(null)
    val themeMode = themeModeFlow.asStateFlow()

    fun update(themeMode: ThemeMode?) {
        themeModeFlow.value = themeMode
    }
}