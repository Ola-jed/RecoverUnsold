package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.requests.LocationCreateOrUpdateRequest
import com.ola.recoverunsold.api.services.LocationService
import com.ola.recoverunsold.api.services.NoContentResponse
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.misc.toMultipartRequestBody
import retrofit2.Response

class LocationServiceWrapper(private val locationService: LocationService) {
    suspend fun getDistributorLocations(
        distributorId: String,
        paginationQuery: PaginationQuery
    ): Response<Page<Location>> {
        return locationService.getDistributorLocations(distributorId, paginationQuery.toQueryMap())
    }

    suspend fun getLocations(
        bearerToken: String,
        paginationQuery: PaginationQuery
    ): Response<Page<Location>> {
        return locationService.getLocations(bearerToken, paginationQuery.toQueryMap())
    }

    suspend fun searchLocations(
        paginationQuery: PaginationQuery,
        searchQuery: String
    ): Response<Page<Location>> {
        return locationService.searchLocations(searchQuery, paginationQuery.toQueryMap())
    }

    suspend fun getLocation(id: String): Response<Location> {
        return locationService.getLocation(id)
    }

    suspend fun createLocation(
        bearerToken: String,
        locationCreateOrUpdateRequest: LocationCreateOrUpdateRequest
    ): Response<Location> {
        return locationService.createLocation(
            bearerToken,
            locationCreateOrUpdateRequest.image,
            locationCreateOrUpdateRequest.indication?.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.name.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.latitude.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.longitude.toMultipartRequestBody()
        )
    }

    suspend fun updateLocation(
        bearerToken: String,
        id: String,
        locationCreateOrUpdateRequest: LocationCreateOrUpdateRequest
    ): NoContentResponse {
        return locationService.updateLocation(
            bearerToken,
            id,
            locationCreateOrUpdateRequest.image,
            locationCreateOrUpdateRequest.indication?.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.name.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.latitude.toMultipartRequestBody(),
            locationCreateOrUpdateRequest.longitude.toMultipartRequestBody()
        )
    }

    suspend fun deleteLocation(
        bearerToken: String,
        id: String
    ): NoContentResponse {
        return locationService.deleteLocation(bearerToken, id)
    }
}