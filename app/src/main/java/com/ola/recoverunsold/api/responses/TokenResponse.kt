package com.ola.recoverunsold.api.responses

import java.util.Date

data class TokenResponse(
    val role: String,
    val token: String,
    val expirationDate: Date
)