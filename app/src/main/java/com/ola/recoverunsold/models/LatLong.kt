package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatLong(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
) {
    companion object {
        val zero: LatLong = LatLong(0.0, 0.0)
    }

    override fun toString(): String = "$latitude,$longitude"
}