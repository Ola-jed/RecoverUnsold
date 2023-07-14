package com.ola.recoverunsold.utils.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NextWeek
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.maps.model.LatLng
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.DistributorHomeData
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.theme.onSuccess
import com.ola.recoverunsold.ui.theme.onWarning
import com.ola.recoverunsold.ui.theme.success
import com.ola.recoverunsold.ui.theme.warning
import com.ola.recoverunsold.utils.resources.Strings
import me.bytebeats.views.charts.bar.BarChartData
import java.util.Date

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

/**
 * Convert Google Map LatLng to a LatLong format used in our app
 */
fun LatLng.toCoordinates(): LatLong = LatLong(
    this.latitude,
    this.longitude
)

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
        OrderStatus.Pending -> MaterialTheme.colorScheme.warning
        OrderStatus.Approved -> MaterialTheme.colorScheme.primary
        OrderStatus.Rejected -> MaterialTheme.colorScheme.error
        OrderStatus.Completed -> MaterialTheme.colorScheme.success
    }
}

@Composable
fun OrderStatus.foregroundColor(): Color {
    return when (this) {
        OrderStatus.Pending -> MaterialTheme.colorScheme.onWarning
        OrderStatus.Approved -> MaterialTheme.colorScheme.onPrimary
        OrderStatus.Rejected -> MaterialTheme.colorScheme.onError
        OrderStatus.Completed -> MaterialTheme.colorScheme.onSuccess
    }
}

/**
 * Get the label to display or this offer
 */
fun Offer.statusLabel(): String {
    val now = Date()
    return if (startDate.after(now)) {
        Strings.get(R.string.coming_soon)
    } else if (startDate.addSeconds(duration).before(now)) {
        Strings.get(R.string.ended)
    } else {
        Strings.get(R.string.ongoing)
    }
}

fun Offer.statusIcon(): ImageVector {
    val now = Date()
    return if (startDate.after(now)) {
        Icons.Default.NextWeek
    } else if (startDate.addSeconds(duration).before(now)) {
        Icons.Default.EventBusy
    } else {
        Icons.Default.EventAvailable
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