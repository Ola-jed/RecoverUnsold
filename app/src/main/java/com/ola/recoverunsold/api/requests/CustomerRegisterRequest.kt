package com.ola.recoverunsold.api.requests

data class CustomerRegisterRequest(
    val username: String,
    val email: String,
    val password: String
)