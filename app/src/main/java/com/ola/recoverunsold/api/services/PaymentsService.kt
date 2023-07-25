package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.TransactionRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentsService : BaseApiService {
    @POST("${ApiUrls.ordersUrl}/{id}/Pay")
    suspend fun payOrder(
        @Path("id") id: String,
        @Body transactionRequest: TransactionRequest
    ): NoContentResponse
}