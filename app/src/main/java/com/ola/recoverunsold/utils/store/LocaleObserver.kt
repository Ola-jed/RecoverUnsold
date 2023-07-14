package com.ola.recoverunsold.utils.store

import com.ola.recoverunsold.utils.enums.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object LocaleObserver {
    private val localeFlow = MutableStateFlow<Locale?>(null)
    val locale = localeFlow.asStateFlow()

    fun update(locale: Locale?) {
        localeFlow.value = locale
    }
}