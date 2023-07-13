package com.ola.recoverunsold.utils.misc

enum class Locale(val code: String) {
    Fr("fr"),
    En("en"),
    Es("es"),
    Auto("");

    companion object {
        fun getByCode(code: String): Locale = when (code) {
            "fr" -> Fr
            "en" -> En
            "es" -> Es
            else -> Auto
        }
    }
}