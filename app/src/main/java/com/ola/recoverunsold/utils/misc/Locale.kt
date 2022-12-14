package com.ola.recoverunsold.utils.misc

enum class Locale(val code: String) {
    Fr("fr"),
    En("en"),
    Es("es"),
    Auto("");

    companion object {
        private val VALUES = values()
        fun getByCode(code: String) = VALUES.firstOrNull { it.code == code } ?: Auto
    }
}