package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.OrderCreateRequest
import com.ola.recoverunsold.models.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService : BaseApiService {
    @POST(ApiUrls.offersUrl + "/{id}" + "/Orders")
    suspend fun createOrder(
        @Header("Authorization") authorization: String,
        @Path("id") offerId: String,
        @Body orderCreateRequest: OrderCreateRequest
    ): Response<Order>
}