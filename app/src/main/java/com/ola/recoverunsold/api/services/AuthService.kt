package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.CustomerRegisterRequest
import com.ola.recoverunsold.api.requests.DistributorRegisterRequest
import com.ola.recoverunsold.api.requests.LoginRequest
import com.ola.recoverunsold.api.responses.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService : BaseApiService {
    @POST(ApiUrls.loginUrl)
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<Token>

    @POST(ApiUrls.distributorRegisterUrl)
    suspend fun registerDistributor(
        @Body distributorRegisterRequest: DistributorRegisterRequest
    ): NoContentResponse

    @POST(ApiUrls.customerRegisterUrl)
    suspend fun registerCustomer(
        @Body customerRegisterRequest: CustomerRegisterRequest
    ): NoContentResponse
}