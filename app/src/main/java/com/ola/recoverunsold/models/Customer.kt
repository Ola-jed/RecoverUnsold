package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Customer(
    @Json(name = "id") override val id: String,
    @Json(name = "username") override val username: String,
    @Json(name = "email") override val email: String,
    @Json(name = "firstName") val firstName: String? = null,
    @Json(name = "lastName") val lastName: String? = null,
    @Json(name = "emailVerifiedAt") override val emailVerifiedAt: Date? = null,
    @Json(name = "createdAt") override val createdAt: Date
) : User