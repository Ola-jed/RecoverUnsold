package com.ola.recoverunsold.models

import java.util.Date

data class DistributorHomeData(
    val ordersPerDay: Map<Date, Int>,
    val orders: List<Order>
)
