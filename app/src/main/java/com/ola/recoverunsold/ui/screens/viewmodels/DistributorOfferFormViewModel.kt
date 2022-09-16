package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.requests.OfferCreateRequest
import com.ola.recoverunsold.api.requests.OfferUpdateRequest
import com.ola.recoverunsold.api.requests.ProductCreateData
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.validation.FormState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class DistributorOfferFormViewModel @AssistedInject constructor(
    private val offerServiceWrapper: OfferServiceWrapper,
    private val locationServiceWrapper: LocationServiceWrapper,
    @Assisted private val offer: Offer?
) : ViewModel() {
    private val bearerToken = TokenStore.get()!!.bearerToken
    private var locationPaginationQuery by mutableStateOf(PaginationQuery(perPage = 50))
    private var locationsResponse by mutableStateOf(ApiStatus.INACTIVE)
    var formState by mutableStateOf(FormState())
    var locations: MutableList<Location> = mutableStateListOf()
    var offerResponse: ApiCallResult<Offer> by mutableStateOf(ApiCallResult.Inactive)
    var startDate by mutableStateOf(offer?.startDate)
    var endDate by mutableStateOf(
        if (offer != null) {
            Date.from(offer.startDate.toInstant().plusSeconds(offer.duration.toLong()))
        } else {
            null
        }
    )
    var beneficiaries by mutableStateOf(offer?.beneficiaries ?: 0)
    var price by mutableStateOf(offer?.price ?: 0.0)
    var location by mutableStateOf(offer?.location)
    var products: MutableList<ProductCreateData> = mutableStateListOf()

    init {
        fetchPublishedLocations()
    }

    private fun fetchPublishedLocations() {
        locationsResponse = ApiStatus.LOADING
        viewModelScope.launch {
            val response = locationServiceWrapper.getLocations(
                bearerToken,
                locationPaginationQuery
            )
            if (response.isSuccessful) {
                val responsePage = response.body()!!
                locations.addAll(responsePage.items)
                locationPaginationQuery = locationPaginationQuery++
                locationsResponse = ApiStatus.SUCCESS
            } else {
                locationsResponse = ApiStatus.ERROR
            }
        }
    }

    fun create() {
        offerResponse = ApiCallResult.Loading
        val offerCreateRequest = OfferCreateRequest(
            startDate = startDate!!,
            duration = (endDate!!.time.toULong() - startDate!!.time.toULong()) / 1000UL,
            beneficiaries = if (beneficiaries == 0) null else beneficiaries,
            price = price,
            locationId = location!!.id
        )
        viewModelScope.launch {
            offerResponse = offerServiceWrapper
                .createOffer(bearerToken, offerCreateRequest)
                .toApiCallResult()
        }
    }

    fun update() {
        offerResponse = ApiCallResult.Loading
        val offerUpdateRequest = OfferUpdateRequest(
            startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(startDate!!),
            duration = (endDate!!.time.toULong() - startDate!!.time.toULong()) / 1000UL,
            beneficiaries = if (beneficiaries == 0) null else beneficiaries,
            price = price,
            locationId = location!!.id
        )
        viewModelScope.launch {
            val response = offerServiceWrapper.updateOffer(
                bearerToken,
                offer!!.id,
                offerUpdateRequest
            )
            offerResponse = if (response.isSuccessful) {
                ApiCallResult.Success(_data = offer)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? {
        return when {
            locationsResponse == ApiStatus.ERROR -> {
                Strings.get(R.string.existing_locations_fetch_failed)
            }
            offerResponse.status == ApiStatus.ERROR -> {
                when (offerResponse.statusCode) {
                    StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
                    StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
                    else -> Strings.get(R.string.unknown_error_occured)
                }
            }
            else -> null
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(offer: Offer?): DistributorOfferFormViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            distributorOfferFormViewModelFactory: Factory,
            offer: Offer?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return distributorOfferFormViewModelFactory.create(offer) as T
            }
        }
    }
}