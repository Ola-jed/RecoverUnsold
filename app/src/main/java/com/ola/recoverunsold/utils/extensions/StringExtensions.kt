package com.ola.recoverunsold.utils.extensions

import java.math.BigInteger
import java.security.MessageDigest
import java.text.DateFormat
import java.util.Date
import java.util.Locale

private val dateTimeFormatter = DateFormat
    .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

/**
 * Returns a new String obtained by removing all the occurrences of a specific String
 */
fun String.remove(partToRemove: String): String = this.replace(partToRemove, "")

/**
 * Returns null if the String is blank, else returns the original string
 */
fun String.nullIfBlank(): String? {
    return ifBlank { null }
}

/**
 * Compute the md hash of the given string
 */
fun String.md5(): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    return BigInteger(1, messageDigest.digest(this.toByteArray())).toString(16)
}

/**
 * Parse a string to a Date object using the defined format in the app
 */
fun String.toDate(): Date? = dateTimeFormatter.parse(this)

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