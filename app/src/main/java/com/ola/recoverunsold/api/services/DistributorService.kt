package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.models.DistributorLabel
import com.ola.recoverunsold.models.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface DistributorService: BaseApiService {
    @GET(ApiUrls.distributorsUrl)
    suspend fun getDistributors(
        @QueryMap filters: Map<String, String>
    ): Response<Page<DistributorInformation>>

    @GET("${ApiUrls.distributorsUrl}/{id}")
    suspend fun getDistributor(
        @Path("id") id: String
    ): Response<DistributorInformation>

    @GET(ApiUrls.distributorLabelsUrl)
    suspend fun getDistributorsLabels(): Response<List<DistributorLabel>>
}