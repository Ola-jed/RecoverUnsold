package com.ola.recoverunsold.api.core

import com.ola.recoverunsold.App
import com.ola.recoverunsold.BuildConfig
import com.ola.recoverunsold.api.services.BaseApiService
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.utils.adapters.DateAdapter
import com.ola.recoverunsold.utils.adapters.ULongAdapter
import com.ola.recoverunsold.utils.misc.hasNetwork
import com.ola.recoverunsold.utils.misc.httpCache
import com.ola.recoverunsold.utils.store.TokenStore
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.util.Date
import java.util.concurrent.TimeUnit

object ApiClient {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(AlertType.JsonAdapter())
            .add(OrderStatus.JsonAdapter())
            .add(Date::class.java, DateAdapter)
            .add(ULong::class.java, ULongAdapter)
            .build()
    }

    private val logger = HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private val jwtHeaderInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val tokenHeaderValue = TokenStore.get()?.bearerToken ?: return chain.proceed(request)
            return chain.proceed(
                request.newBuilder()
                    .header("Authorization", tokenHeaderValue)
                    .build()
            )
        }
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(App.instance.httpCache())
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logger)
            .addInterceptor(jwtHeaderInterceptor)
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
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    inline fun <reified T : BaseApiService> buildService(): T = retrofit.create(T::class.java)
}