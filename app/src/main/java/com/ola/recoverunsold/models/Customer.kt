package com.ola.recoverunsold.models

import java.util.Date

data class Customer(
    override val username: String,
    override val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    override val emailVerifiedAt: Date? = null,
    override val createdAt: Date
) : User