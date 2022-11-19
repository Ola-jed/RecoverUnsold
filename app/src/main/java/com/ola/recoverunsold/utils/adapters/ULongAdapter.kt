package com.ola.recoverunsold.utils.adapters

import com.ola.recoverunsold.api.core.ApiClient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

object ULongAdapter : JsonAdapter<ULong>() {
    private val adapter: JsonAdapter<String> = ApiClient.moshi.adapter(String::class.java)

    override fun fromJson(reader: JsonReader): ULong? = adapter.fromJson(reader)?.toULong()

    override fun toJson(writer: JsonWriter, value: ULong?) =
        adapter.toJson(writer, value?.toString())
}