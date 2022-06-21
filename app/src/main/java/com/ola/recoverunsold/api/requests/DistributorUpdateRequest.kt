package com.ola.recoverunsold.api.requests

data class DistributorUpdateRequest(
    val username: String,
    val phone: String,
    val taxId: String,
    val rccm: String,
    val websiteUrl: String?
)