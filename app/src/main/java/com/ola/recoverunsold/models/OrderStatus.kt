package com.ola.recoverunsold.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

enum class OrderStatus {
    Pending,
    Approved,
    Rejected,
    Completed;

    internal class Serializer : JsonDeserializer<OrderStatus> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): OrderStatus {
            return when (json?.asInt) {
                0 -> Pending
                1 -> Approved
                2 -> Rejected
                3 -> Completed
                else -> throw IllegalArgumentException("Unknown OrderStatus")
            }
        }
    }
}