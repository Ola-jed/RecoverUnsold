package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class OfferCreateRequest(
    @Json(name = "startDate") val startDate: Date,
    @Json(name = "duration") val duration: ULong,
    @Json(name = "onlinePayment") val onlinePayment: Boolean,
    @Json(name = "beneficiaries") val beneficiaries: Int? = null,
    @Json(name = "price") val price: Double,
    @Json(name = "locationId") val locationId: String
)