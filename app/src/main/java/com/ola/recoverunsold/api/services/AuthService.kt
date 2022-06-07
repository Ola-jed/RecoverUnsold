package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.ApiConstants
import com.ola.recoverunsold.api.requests.CustomerRegisterRequest
import com.ola.recoverunsold.api.requests.DistributorRegisterRequest
import com.ola.recoverunsold.api.requests.LoginRequest
import com.ola.recoverunsold.api.responses.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService : BaseApiService {
    @POST(ApiConstants.loginUrl)
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @POST(ApiConstants.distributorRegisterUrl)
    suspend fun registerDistributor(@Body distributorRegisterRequest: DistributorRegisterRequest): Response<Void>

    @POST(ApiConstants.customerRegisterUrl)
    suspend fun registerCustomer(@Body customerRegisterRequest: CustomerRegisterRequest): Response<Void>
}