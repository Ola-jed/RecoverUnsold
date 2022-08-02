package com.ola.recoverunsold.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

enum class AlertType {
    AnyOfferPublished,
    DistributorOfferPublished;

    internal class Serializer : JsonDeserializer<AlertType>, JsonSerializer<AlertType> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): AlertType {
            return when (json?.asInt) {
                0 -> AnyOfferPublished
                1 -> DistributorOfferPublished
                else -> throw IllegalArgumentException("Unknown OrderStatus")
            }
        }

        override fun serialize(
            src: AlertType?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            if (src == null) {
                throw NullPointerException("Cannot serialize null value")
            }

            return JsonPrimitive(
                when (src) {
                    AnyOfferPublished -> 0
                    DistributorOfferPublished -> 1
                }
            )
        }
    }
}