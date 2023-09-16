package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repayment(
    @Json(name = "id") val id: String,
    @Json(name = "done") val done: Boolean,
    @Json(name = "note") val note: String? = null,
    @Json(name = "transactionId") val transactionId: String? = null,
    @Json(name = "order") val order: Order? = null
)