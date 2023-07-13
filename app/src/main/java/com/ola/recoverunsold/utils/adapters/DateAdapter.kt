package com.ola.recoverunsold.utils.adapters

import com.ola.recoverunsold.api.core.ApiClient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateAdapter : JsonAdapter<Date>() {
    private val adapter: JsonAdapter<String> = ApiClient.moshi.adapter(String::class.java)
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    override fun fromJson(reader: JsonReader): Date? =
        adapter.fromJson(reader)?.let { simpleDateFormat.parse(it) }

    override fun toJson(writer: JsonWriter, value: Date?) =
        adapter.toJson(writer, value?.let { simpleDateFormat.format(it) })
}