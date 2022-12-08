package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "id") val id: String,
    @Json(name = "status") val status: OrderStatus,
    @Json(name = "customer") val customer: Customer? = null,
    @Json(name = "offerId") val offerId: String,
    @Json(name = "offer") val offer: Offer?,
    @Json(name = "payment") val payment: Payment?,
    @Json(name = "withdrawalDate") val withdrawalDate: Date,
    @Json(name = "createdAt") val createdAt: Date,
    @Json(name = "opinions") val opinions: List<Opinion>
)