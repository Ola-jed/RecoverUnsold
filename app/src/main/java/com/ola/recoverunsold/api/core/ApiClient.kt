package com.ola.recoverunsold.api.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ola.recoverunsold.App
import com.ola.recoverunsold.BuildConfig
import com.ola.recoverunsold.api.services.BaseApiService
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.utils.misc.hasNetwork
import com.ola.recoverunsold.utils.misc.httpCache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object ApiClient {
    private val gson: Gson by lazy {
        GsonBuilder().setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeAdapter(OrderStatus::class.java, OrderStatus.Serializer())
            .registerTypeAdapter(AlertType::class.java, AlertType.Serializer())
            .create()
    }

    private val logger = HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(App.instance.httpCache())
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logger)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header(
                            "Cache-Control", if (App.instance.hasNetwork()) {
                                "public, max-age=5"
                            } else {
                                "public, only-if-cached, max-stale=604800"
                            }
                        ).build()
                )
            }
            .build()
    }

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                converterFactory(),
                type,
                annotations
            )

            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                nextResponseBodyConverter.convert(value)
            } else {
                null
            }
        }
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiUrls.apiBaseUrl)
            .client(httpClient)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    inline fun <reified T : BaseApiService> buildService(): T = retrofit.create(T::class.java)
}