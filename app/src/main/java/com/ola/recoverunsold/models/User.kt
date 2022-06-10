package com.ola.recoverunsold.models

import java.util.Date

sealed interface User {
    val username: String
    val email: String
    val emailVerifiedAt: Date?
    val createdAt: Date
}