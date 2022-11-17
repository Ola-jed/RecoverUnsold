package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.models.CustomerHomeData
import com.ola.recoverunsold.models.DistributorHomeData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface HomeService : BaseApiService {
    @GET(ApiUrls.homeUrl)
    suspend fun getCustomerHomeData(): Response<CustomerHomeData>

    @GET(ApiUrls.distributorsHomeUrl)
    suspend fun getDistributorHomeData(
        @QueryMap period: Map<String, String>
    ): Response<DistributorHomeData>
}