package com.ola.recoverunsold.utils.extensions

import com.ola.recoverunsold.api.core.ApiClient

/**
 * Deserialize a Json String to a specific type
 */
inline fun <reified T> String?.jsonDeserialize(): T? {
    return if (this.isNullOrBlank()) {
        null
    } else {
        ApiClient.moshi.adapter(T::class.java).fromJson(this)
    }
}

/**
 * Serialize any object to a json String
 */
inline fun <reified T> T.jsonSerialize(): String {
    return ApiClient.moshi.adapter(T::class.java).toJson(this)
}