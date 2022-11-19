package com.ola.recoverunsold.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class OrderStatus {
    Pending,
    Approved,
    Rejected,
    Completed;

    internal class JsonAdapter {
        @ToJson
        fun toJson(orderStatus: OrderStatus): Int {
            return when (orderStatus) {
                Pending -> 0
                Approved -> 1
                Rejected -> 2
                Completed -> 3
            }
        }

        @FromJson
        fun fromJson(value: Int): OrderStatus {
            return when (value) {
                0 -> Pending
                1 -> Approved
                2 -> Rejected
                3 -> Completed
                else -> throw IllegalArgumentException("Unknown OrderStatus")
            }
        }
    }
}