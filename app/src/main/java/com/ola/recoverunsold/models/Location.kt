package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "coordinates") val coordinates: LatLong,
    @Json(name = "indication") val indication: String? = null,
    @Json(name = "image") val image: String? = null,
    @Json(name = "createdAt") val createdAt: Date
) {
    companion object {
        val Dummy: Location = Location("", "", LatLong.zero, createdAt = Date())
    }
}