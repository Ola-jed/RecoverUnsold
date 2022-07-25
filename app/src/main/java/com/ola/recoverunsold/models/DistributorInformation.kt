package com.ola.recoverunsold.models

import java.util.Date

data class DistributorInformation(
    val id: String,
    val username: String,
    val email: String,
    val phone: String,
    val websiteUrl: String? = null,
    val createdAt: Date,
    val offers: List<Offer>? = null
)