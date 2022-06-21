package com.ola.recoverunsold.api.requests

data class DistributorUpdateRequest(
    val taxId: String,
    val phone: String,
    val rccm: String,
    val username: String,
    val websiteUrl: String?
)