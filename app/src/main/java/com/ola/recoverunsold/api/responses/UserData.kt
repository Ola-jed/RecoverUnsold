package com.ola.recoverunsold.api.responses

import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "id") val id: String,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "firstName") val firstName: String? = null,
    @Json(name = "lastName") val lastName: String? = null,
    @Json(name = "role") val role: String,
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "taxId") val taxId: String? = null,
    @Json(name = "rccm") val rccm: String? = null,
    @Json(name = "websiteUrl") val websiteUrl: String? = null,
    @Json(name = "emailVerifiedAt") val emailVerifiedAt: Date? = null,
    @Json(name = "createdAt") val createdAt: Date
) {
    fun customer(): Customer {
        require(role == TokenRoles.CUSTOMER)
        return Customer(
            id,
            username,
            email,
            firstName,
            lastName,
            emailVerifiedAt,
            createdAt
        )
    }

    fun distributor(): Distributor {
        require(role == TokenRoles.DISTRIBUTOR)
        return Distributor(
            id,
            username,
            email,
            phone!!,
            taxId!!,
            rccm!!,
            websiteUrl,
            emailVerifiedAt,
            createdAt
        )
    }
}
