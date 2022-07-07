package com.ola.recoverunsold.utils.misc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.ColorInt
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.core.content.getSystemService
import androidx.core.graphics.ColorUtils
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.utils.resources.Strings
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

private val dateTimeFormatter = DateFormat
    .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

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
 * Shortcut for snackbar showing
 * Because the duration is always long and the dismiss message "OK"
 */
suspend fun SnackbarHostState.show(message: String): SnackbarResult {
    return showSnackbar(
        message = message,
        actionLabel = Strings.get(R.string.ok),
        duration = SnackbarDuration.Long
    )
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

/**
 * Create a file from an Uri
 * A tricky process
 */
fun Uri.createFile(context: Context): File {
    var fileName = ""
    val contentResolver = context.contentResolver
    this.let { returnUri ->
        contentResolver.query(returnUri, null, null, null)
    }?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }

    val fileType: String? = this.let { returnUri ->
        contentResolver.getType(returnUri)
    }

    val iStream: InputStream = contentResolver.openInputStream(this)!!
    val outputDir: File = context.cacheDir!!
    val outputFile = File(outputDir, fileName)
    copyStreamToFile(iStream, outputFile)
    iStream.close()
    return outputFile
}

fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}

/**
 * Format a date using the locale default format
 */
fun Date.formatDate(): String {
    return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(this)
}

/**
 * Format a date to DateTime using the default locale
 */
fun Date.formatDateTime(): String = dateTimeFormatter.format(this)

/**
 * Parse a string to a Date object using the defined format in the app
 */
fun String.toDate(): Date? = dateTimeFormatter.parse(this)

/**
 * Format a date following a format for storing and exchanging dates
 */
fun Date.format(format: String = "yyyy-MM-dd'T'HH:mm:ss.SSS"): String {
    return SimpleDateFormat(format).format(this)
}

/**
 * Format decimal numbers without the zeros at the end
 */
fun Number?.formatWithoutTrailingZeros(): String {
    // Special case : 0/0.00/00.00000 ...
    if (this?.toLong() == 0L) {
        return "0"
    }
    // Integral number
    val toString = this?.toString()
    if (toString?.contains('.') == false) {
        return toString
    }
    // No decimal part
    if (toString?.split('.')?.get(1)?.toInt() == 0) {
        return toString.split('.')[0]
    }
    // An empty decimal part
    if(toString?.endsWith('.') == true) {
        return "$toString.0"
    }
    return toString?.replace("^(\\d+\\.\\d*?[1-9])0+\$", "$1") ?: ""
}

/**
 * Convert a String to double without throwing an exception
 * Handling edge cases such as blank String
 */
fun String?.toSecureDouble(): Double {
    if (this.isNullOrBlank()) return 0.0
    return try {
        this.toDouble()
    } catch (e: Exception) {
        if (this.endsWith('.')) {
            return ("${this}0").toSecureDouble()
        }

        0.0
    }
}

/**
 * Convert a String to double without throwing an exception
 * Handling edge cases such as blank String
 */
fun String?.toSecureInt(): Int {
    if (this.isNullOrBlank()) return 0
    return try {
        this.toInt()
    } catch (e: Exception) {
        if (!this.last().isDigit()) {
            return this.dropLast(1).toSecureInt()
        }

        0
    }
}