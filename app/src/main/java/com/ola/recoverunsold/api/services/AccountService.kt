package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.api.requests.CustomerUpdateRequest
import com.ola.recoverunsold.api.requests.PasswordUpdateRequest
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface AccountService : BaseApiService {
    @GET(ApiConstants.accountUrl)
    suspend fun getCustomer(
        @Header("Authorization") authorization: String
    ): Response<Customer>

    @GET(ApiConstants.accountUrl)
    suspend fun getDistributor(
        @Header("Authorization") authorization: String
    ): Response<Distributor>

    @PUT(ApiConstants.customerAccountUrl)
    suspend fun updateCustomer(
        @Header("Authorization") authorization: String,
        @Body customerUpdateRequest: CustomerUpdateRequest
    ): NoContentResponse

    @PUT(ApiConstants.distributorAccountUrl)
    suspend fun updateDistributor(
        @Header("Authorization") authorization: String,
        @Body distributorUpdateRequest: CustomerUpdateRequest
    ): NoContentResponse

    @PUT(ApiConstants.accountPasswordUrl)
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Body passwordUpdateRequest: PasswordUpdateRequest
    ): NoContentResponse

    @DELETE(ApiConstants.accountUrl)
    suspend fun deleteAccount(
        @Header("Authorization") authorization: String
    ): NoContentResponse
}