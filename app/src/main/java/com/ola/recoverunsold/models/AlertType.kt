package com.ola.recoverunsold.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class AlertType {
    AnyOfferPublished,
    DistributorOfferPublished;

    internal class JsonAdapter {
        @ToJson
        fun toJson(alertType: AlertType): Int {
            return when (alertType) {
                AnyOfferPublished -> 0
                DistributorOfferPublished -> 1
            }
        }

        @FromJson
        fun fromJson(value: Int): AlertType {
            return when (value) {
                0 -> AnyOfferPublished
                1 -> DistributorOfferPublished
                else -> throw IllegalArgumentException("Unknown AlertType")
            }
        }
    }
}