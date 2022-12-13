package com.ola.recoverunsold.ui.theme

enum class ThemeMode(val value: Int) {
    Light(0),
    Dark(1),
    Auto(2);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int) = VALUES.first { it.value == value }
    }
}