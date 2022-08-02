package com.ola.recoverunsold.api.requests

import com.ola.recoverunsold.models.AlertType

data class AlertCreateRequest(
    val alertType: AlertType,
    val distributorId: String? = null
)
