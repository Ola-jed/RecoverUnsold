package com.ola.recoverunsold.utils.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Format decimal numbers without the zeros at the end
 */
fun Number?.formatWithoutTrailingZeros(): String {
    if (this == null) return ""
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 340
    return df.format(this)
}