package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.DistributorFilterQuery
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.models.Page
import retrofit2.Response

class DistributorServiceWrapper(private val distributorService: DistributorService) {
    suspend fun getDistributors(
        distributorFilterQuery: DistributorFilterQuery
    ): Response<Page<DistributorInformation>> {
        return distributorService.getDistributors(distributorFilterQuery.toQueryMap())
    }

    suspend fun getDistributor(id: String): Response<DistributorInformation> {
        return distributorService.getDistributor(id)
    }
}