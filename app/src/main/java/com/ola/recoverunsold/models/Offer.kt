package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Offer(
    @Json(name = "id") val id: String,
    @Json(name = "startDate") val startDate: Date,
    @Json(name = "duration") val duration: ULong,
    @Json(name = "onlinePayment") val onlinePayment: Boolean,
    @Json(name = "beneficiaries") val beneficiaries: Int? = null,
    @Json(name = "price") val price: Double,
    @Json(name = "createdAt") val createdAt: Date,
    @Json(name = "distributorId") val distributorId: String,
    @Json(name = "location") val location: Location? = null,
    @Json(name = "products") val products: List<Product>? = null
)