package com.ola.recoverunsold.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "role") val role: String,
    @Json(name = "token") val token: String,
    @Json(name = "expirationDate") val expirationDate: Date
) {
    val bearerToken get() = "Bearer $token"
}

object TokenRoles {
    const val CUSTOMER = "Customer"
    const val DISTRIBUTOR = "Distributor"
}