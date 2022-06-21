package com.ola.recoverunsold.api.requests

data class CustomerUpdateRequest(
    val username: String,
    val firstName: String,
    val lastName: String
)