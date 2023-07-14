package com.ola.recoverunsold.utils.extensions

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private val dateTimeFormatter = DateFormat
    .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

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
 * Format a date following a format for storing and exchanging dates
 */
fun Date.format(format: String = "yyyy-MM-dd'T'HH:mm:ss.SSS"): String {
    return SimpleDateFormat(format).format(this)
}

fun Date.toLocalDate(): LocalDate {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault()).toLocalDate()!!
}

fun LocalDate.toDate(): Date {
    return Date.from(
        LocalDateTime.of(this, LocalTime.MIDNIGHT)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    )
}