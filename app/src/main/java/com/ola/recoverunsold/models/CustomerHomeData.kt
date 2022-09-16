package com.ola.recoverunsold.models

data class CustomerHomeData(
    val offers: List<Offer>,
    val distributors: List<DistributorInformation>,
    val orderStats: CustomerOrderStats? = null
)