package com.ola.recoverunsold.models

data class Alert(
    val id: String,
    val alertType: AlertType,
    val distributorInformation: DistributorInformation? = null
)