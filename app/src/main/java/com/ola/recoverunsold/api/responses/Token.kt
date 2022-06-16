package com.ola.recoverunsold.api.responses

import java.util.Date

data class Token(
    val role: String,
    val token: String,
    val expirationDate: Date
) {
    val bearerToken get() = "Bearer $token"
}

object TokenRoles {
    const val CUSTOMER = "Customer"
    const val DISTRIBUTOR = "Distributor"
}