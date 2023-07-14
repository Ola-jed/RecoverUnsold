package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.OfferDistanceFilterQuery
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.api.requests.OfferCreateRequest
import com.ola.recoverunsold.api.requests.OfferUpdateRequest
import com.ola.recoverunsold.api.services.NoContentResponse
import com.ola.recoverunsold.api.services.OfferService
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.extensions.toMultipartRequestBody
import retrofit2.Response
import java.text.SimpleDateFormat

class OfferServiceWrapper(private val offerService: OfferService) {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    suspend fun getDistributorOffers(
        distributorId: String,
        offerFilterQuery: OfferFilterQuery
    ): Response<Page<Offer>> {
        return offerService.getDistributorOffers(distributorId, offerFilterQuery.toQueryMap())
    }

    suspend fun getOffers(offerFilterQuery: OfferFilterQuery): Response<Page<Offer>> {
        return offerService.getOffers(offerFilterQuery.toQueryMap())
    }

    suspend fun getCloseOffers(
        offerDistanceFilterQuery: OfferDistanceFilterQuery
    ): Response<Page<OfferWithRelativeDistance>> {
        return offerService.getCloseOffers(offerDistanceFilterQuery.toQueryMap())
    }

    suspend fun getOffer(id: String): Response<Offer> {
        return offerService.getOffer(id)
    }

    suspend fun createOffer(
        offerCreateRequest: OfferCreateRequest
    ): Response<Offer> {
        return offerService.createOffer(
            formatter.format(offerCreateRequest.startDate).toMultipartRequestBody(),
            offerCreateRequest.duration.toMultipartRequestBody(),
            offerCreateRequest.beneficiaries?.toMultipartRequestBody(),
            offerCreateRequest.price.toMultipartRequestBody(),
            offerCreateRequest.locationId.toMultipartRequestBody(),
        )
    }

    suspend fun updateOffer(
        id: String,
        offerUpdateRequest: OfferUpdateRequest
    ): NoContentResponse {
        return offerService.updateOffer(id, offerUpdateRequest)
    }

    suspend fun deleteOffer(id: String): NoContentResponse {
        return offerService.deleteOffer(id)
    }
}