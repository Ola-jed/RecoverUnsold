package com.ola.recoverunsold.api.requests

data class DistributorRegisterRequest(
    val email: String,
    val ifu: String,
    val password: String,
    val phone: String,
    val rccm: String,
    val username: String,
    val websiteUrl: String
)