package com.ola.recoverunsold.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class AuthenticationResult(
    @Json(name = "token") val token: String,
    @Json(name = "userData") val userData: UserData,
    @Json(name = "expirationDate") val expirationDate: Date
) {
    val appToken = Token(userData.role, token, expirationDate)
}
