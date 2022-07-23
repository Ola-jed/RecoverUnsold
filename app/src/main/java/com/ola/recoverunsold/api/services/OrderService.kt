package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.OrderCreateRequest
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface OrderService : BaseApiService {
    @GET(ApiUrls.ordersUrl + "/{id}")
    suspend fun getOrder(
        @Path("id") id: String
    ): Response<Order>

    @GET(ApiUrls.customerOrdersUrl)
    suspend fun getCustomerOrders(
        @Header("Authorization") authorization: String,
        @QueryMap filters: Map<String, String>
    ): Response<Page<Order>>

    @GET(ApiUrls.distributorOrdersUrl)
    suspend fun getDistributorOrders(
        @Header("Authorization") authorization: String,
        @QueryMap filters: Map<String, String>
    ): Response<Page<Order>>

    @GET(ApiUrls.offersUrl + "/{id}" + "/Orders")
    suspend fun getOfferOrders(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @QueryMap filters: Map<String, String>
    ): Response<Page<Order>>

    @POST(ApiUrls.offersUrl + "/{id}" + "/Orders")
    suspend fun createOrder(
        @Header("Authorization") authorization: String,
        @Path("id") offerId: String,
        @Body orderCreateRequest: OrderCreateRequest
    ): Response<Order>

    @POST(ApiUrls.ordersUrl + "/{id}/Accept")
    suspend fun acceptOrder(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NoContentResponse

    @POST(ApiUrls.ordersUrl + "/{id}/Reject")
    suspend fun rejectOrder(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NoContentResponse
}