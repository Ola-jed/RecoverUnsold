package com.ola.recoverunsold.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ola.recoverunsold.api.services.BaseApiService
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object ApiClient {
    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
    }

    private val logger = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object :
            Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                nextResponseBodyConverter.convert(value)
            } else {
                null
            }
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.apiBaseUrl)
            .client(httpClient)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T : BaseApiService> buildService(service: Class<T>): T = retrofit.create(service)
}