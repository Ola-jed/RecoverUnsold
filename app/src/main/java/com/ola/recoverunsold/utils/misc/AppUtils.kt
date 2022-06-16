package com.ola.recoverunsold.utils.misc

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.ola.recoverunsold.utils.store.AppUserStore
import com.ola.recoverunsold.utils.store.TokenStore
import kotlin.math.absoluteValue

@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}

suspend fun Context.logout() {
    TokenStore(this).removeToken()
    AppUserStore(this).removeUser()
}