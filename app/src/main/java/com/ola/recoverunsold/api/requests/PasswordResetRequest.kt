package com.ola.recoverunsold.api.requests

data class PasswordResetRequest(
    val password: String,
    val token: String
)