package com.ola.recoverunsold.models

import java.util.Date

data class Distributor(
    override val id: String,
    override val username: String,
    override val email: String,
    val phone: String,
    val taxId: String,
    val rccm: String,
    val websiteUrl: String? = null,
    override val emailVerifiedAt: Date? = null,
    override val createdAt: Date
) : User