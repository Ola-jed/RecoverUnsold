package com.ola.recoverunsold.api.requests

data class PasswordUpdateRequest(val newPassword: String, val oldPassword: String)
