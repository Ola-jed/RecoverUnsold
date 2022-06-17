package com.ola.recoverunsold.api.query

interface ApiClientQueryInterface {
    fun toQueryMap(): Map<String,String>
}