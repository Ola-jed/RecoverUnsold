package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.CustomerUpdateRequest
import com.ola.recoverunsold.api.requests.DistributorUpdateRequest
import com.ola.recoverunsold.api.requests.PasswordUpdateRequest
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountService : BaseApiService {
    @GET(ApiUrls.accountUrl)
    suspend fun getCustomer(): Response<Customer>

    @GET(ApiUrls.accountUrl)
    suspend fun getDistributor(): Response<Distributor>

    @PUT(ApiUrls.customerAccountUrl)
    suspend fun updateCustomer(
        @Body customerUpdateRequest: CustomerUpdateRequest
    ): NoContentResponse

    @PUT(ApiUrls.distributorAccountUrl)
    suspend fun updateDistributor(
        @Body distributorUpdateRequest: DistributorUpdateRequest
    ): NoContentResponse

    @PUT(ApiUrls.accountPasswordUrl)
    suspend fun updatePassword(
        @Body passwordUpdateRequest: PasswordUpdateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.accountUrl)
    suspend fun deleteAccount(): NoContentResponse
}