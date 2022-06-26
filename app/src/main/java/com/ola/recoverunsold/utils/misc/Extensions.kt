package com.ola.recoverunsold.utils.misc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.ColorInt
import androidx.core.content.getSystemService
import androidx.core.graphics.ColorUtils
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.ola.recoverunsold.models.LatLong
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.math.absoluteValue

/**
 * Extensions methods for our classes
 */

/**
 * Convert a color string to Hsl
 */
@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}

/**
 * Returns a new String obtained by removing all the occurrences of a specific String
 */
fun String.remove(partToRemove: String): String = this.replace(partToRemove, "")

/**
 * Deserialize a Json String to a specific type
 */
inline fun <reified T> String?.jsonDeserialize(): T? {
    return if (this == null) {
        null
    } else {
        Gson().fromJson(this, T::class.java)
    }
}

/**
 * Serialize any object to a json String
 */
fun <T> T.jsonSerialize(): String {
    return Gson().toJson(this)
}

/**
 * Returns null if the String is blank, else returns the original string
 */
fun String.nullIfBlank(): String? {
    return ifBlank { null }
}

/**
 * Returns if the current Context has network access
 */
fun Context.hasNetwork(): Boolean {
    val connectivityManager = this.getSystemService<ConnectivityManager>()
    if (connectivityManager != null) {
        val capabilities = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
    }
    return false
}

/**
 * Convert a data type to Multipart/Form-data request body element
 */
fun <T> T.toMultipartRequestBody(): RequestBody {
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

/**
 * Convert Google Map LatLng to a LatLong format used in our app
 */
fun LatLng.toCoordinates(): LatLong = LatLong(
    this.latitude,
    this.longitude
)