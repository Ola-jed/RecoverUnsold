package com.ola.recoverunsold.utils.misc

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.ColorInt
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.ColorUtils
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.DistributorHomeData
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.theme.AppCustomColors
import com.ola.recoverunsold.utils.resources.Strings
import me.bytebeats.views.charts.bar.BarChartData
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

private val colorsDeciles = arrayOf(
    Color(red = 255, green = 0, blue = 0),
    Color(red = 229, green = 25, blue = 0),
    Color(red = 204, green = 51, blue = 0),
    Color(red = 178, green = 76, blue = 0),
    Color(red = 153, green = 102, blue = 0),
    Color(red = 127, green = 127, blue = 0),
    Color(red = 102, green = 153, blue = 0),
    Color(red = 76, green = 178, blue = 0),
    Color(red = 51, green = 204, blue = 0),
    Color(red = 25, green = 229, blue = 0)
)
private val dateTimeFormatter = DateFormat
    .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
private const val cacheSize = 5242880L
val gson = Gson()

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
        gson.fromJson(this, T::class.java)
    }
}

/**
 * Serialize any object to a json String
 */
fun <T> T.jsonSerialize(): String {
    return gson.toJson(this)
}

/**
 * Returns null if the String is blank, else returns the original string
 */
fun String.nullIfBlank(): String? {
    return ifBlank { null }
}

/**
 * Use the Android context to get the device location
 */
fun Context.getDeviceLocation(
    onLatLngValueUpdate: (LatLng) -> Unit,
    onLocationFetchFailed: () -> Unit
) {
    // TODO : no more fused
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    val locationChangeListener = fun(location: Location?) {
        if (location != null) {
            onLatLngValueUpdate(LatLng(location.latitude, location.longitude))
        } else {
            onLocationFetchFailed()
        }
    }
    fusedLocationProviderClient.getCurrentLocation(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        null
    ).addOnSuccessListener(locationChangeListener)
}

/**
 * Open a map with the given lat long
 */
fun Context.openMapWithCoordinates(latitude: Double, longitude: Double) {
    val mapsIntent = Intent(Intent.ACTION_VIEW)
    mapsIntent.data = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
    ContextCompat.startActivity(this, mapsIntent, null)
}

/**
 * Returns if the current Context has network access
 */
fun Context.hasNetwork(): Boolean {
    val connectivityManager = this.getSystemService<ConnectivityManager>()
    val capabilities = connectivityManager
        ?.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}

/**
 * Create an http cache for okhttp from the context
 */
fun Context.httpCache(): Cache = Cache(this.cacheDir, cacheSize)

/**
 * Shortcut for snackbar showing
 * Because the duration is always long and the dismiss message "OK"
 */
suspend fun SnackbarHostState.show(message: String): SnackbarResult = showSnackbar(
    message = message,
    actionLabel = Strings.get(R.string.ok),
    duration = SnackbarDuration.Long
)

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

    this.let { returnUri ->
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
 * Add a big amount of time to a Date
 */
fun Date.addSeconds(seconds: ULong): Date {
    var returnDate = this
    var value = seconds
    val longMaxValueAsULong = Long.MAX_VALUE.toULong()
    if (seconds > longMaxValueAsULong) {
        while (value > longMaxValueAsULong) {
            var returnInstant = returnDate.toInstant()
            returnInstant = returnInstant.plusSeconds(Long.MAX_VALUE)
            value -= longMaxValueAsULong
            returnDate = Date.from(returnInstant)
        }
    } else {
        return Date.from(returnDate.toInstant().plusSeconds(seconds.toLong()))
    }
    return returnDate
}

/**
 * Retrieve a big amount of time to a Date
 */
fun Date.minusSeconds(seconds: ULong): Date {
    var returnDate = this
    var value = seconds
    val longMaxValueAsULong = Long.MAX_VALUE.toULong()
    if (seconds > longMaxValueAsULong) {
        while (value > longMaxValueAsULong) {
            var returnInstant = returnDate.toInstant()
            returnInstant = returnInstant.minusSeconds(Long.MAX_VALUE)
            value -= longMaxValueAsULong
            returnDate = Date.from(returnInstant)
        }
    } else {
        return Date.from(returnDate.toInstant().minusSeconds(seconds.toLong()))
    }
    return returnDate
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
    if (this == null) return ""
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 340
    return df.format(this)
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

/**
 * Convert the status of the order to an internationalized string at singular
 */
fun OrderStatus.internationalizedValueSingular(): String {
    return when (this) {
        OrderStatus.Pending -> Strings.get(R.string.order_status_pending)
        OrderStatus.Approved -> Strings.get(R.string.order_status_approved_singular)
        OrderStatus.Rejected -> Strings.get(R.string.order_status_rejected_singular)
        OrderStatus.Completed -> Strings.get(R.string.order_status_completed_singular)
    }
}

/**
 * Convert the status of the order to an internationalized string
 */
fun OrderStatus.internationalizedValue(): String {
    return when (this) {
        OrderStatus.Pending -> Strings.get(R.string.order_status_pending)
        OrderStatus.Approved -> Strings.get(R.string.order_status_approved)
        OrderStatus.Rejected -> Strings.get(R.string.order_status_rejected)
        OrderStatus.Completed -> Strings.get(R.string.order_status_completed)
    }
}

/**
 * Get the icon related to a Status
 */
fun OrderStatus.toIcon(): ImageVector {
    return when (this) {
        OrderStatus.Pending -> Icons.Default.HourglassTop
        OrderStatus.Approved -> Icons.Default.Check
        OrderStatus.Rejected -> Icons.Default.Block
        OrderStatus.Completed -> Icons.Default.DoneAll
    }
}

@Composable
fun OrderStatus.backgroundColor(): Color {
    return when (this) {
        OrderStatus.Pending -> AppCustomColors.warning
        OrderStatus.Approved -> MaterialTheme.colors.primary
        OrderStatus.Rejected -> MaterialTheme.colors.error
        OrderStatus.Completed -> AppCustomColors.success
    }
}

@Composable
fun OrderStatus.foregroundColor(): Color {
    return when (this) {
        OrderStatus.Pending -> AppCustomColors.onWarning
        OrderStatus.Approved -> MaterialTheme.colors.onPrimary
        OrderStatus.Rejected -> MaterialTheme.colors.onError
        OrderStatus.Completed -> AppCustomColors.onSuccess
    }
}

/**
 * Convert the alert type into a meaningful label
 */
fun AlertType.label(): String {
    return when (this) {
        AlertType.AnyOfferPublished -> Strings.get(R.string.all_new_publications)
        AlertType.DistributorOfferPublished -> Strings.get(R.string.publications_of_given_distributor)
    }
}

/**
 * Convert the data of orders for a distributor to bars for the chart
 */
fun DistributorHomeData.toBars(): List<BarChartData.Bar> {
    if (ordersPerDay.isEmpty()) {
        return emptyList()
    }

    val maxOrdersPerDayDivided = (ordersPerDay.values.maxOf { it }) / 10F
    val ordersPerDayDeciles = (1..10).map { maxOrdersPerDayDivided * it }
    return ordersPerDay.map {
        BarChartData.Bar(
            value = it.value.toFloat(),
            label = it.key.formatDate(),
            color = colorsDeciles[ordersPerDayDeciles.indexOf(ordersPerDayDeciles.first { value -> value >= it.value })]
        )
    }.filter { it.value != 0F }
}

fun <T> Response<T>.toApiCallResult(onSuccess: (() -> Unit)? = null): ApiCallResult<T> {
    return if (this.isSuccessful) {
        if (onSuccess != null) {
            onSuccess()
        }
        ApiCallResult.Success(_data = this.body())
    } else {
        ApiCallResult.Error(code = this.code())
    }
}

@JvmName("toApiCallResultVoid")
fun Response<Void>.toApiCallResult(onSuccess: (() -> Unit)? = null): ApiCallResult<Unit> {
    return if (this.isSuccessful) {
        if (onSuccess != null) {
            onSuccess()
        }
        ApiCallResult.Success(_data = Unit)
    } else {
        ApiCallResult.Error(code = this.code())
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }

    return remember(this) {
        derivedStateOf {
            ((previousIndex != firstVisibleItemIndex && previousIndex > firstVisibleItemIndex)
                    || previousScrollOffset >= firstVisibleItemScrollOffset).also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}