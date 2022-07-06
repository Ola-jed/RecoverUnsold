package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.Date

class DistributorOfferFormViewModelFactory(private val offer: Offer?) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DistributorOfferFormViewModel(offer = offer) as T
    }
}

class DistributorOfferFormViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = KoinJavaComponent.get(OfferServiceWrapper::class.java),
    private val locationServiceWrapper: LocationServiceWrapper = KoinJavaComponent.get(
        LocationServiceWrapper::class.java
    ),
    private val offer: Offer?
) : ViewModel() {
    private val token = TokenStore.get()!!
    var formState by mutableStateOf(FormState())
    var locationPaginationQuery by mutableStateOf(PaginationQuery())
    var locationsResponse by mutableStateOf(ApiStatus.INACTIVE)
    var locations: List<Location> by mutableStateOf(emptyList())

    var startDate by mutableStateOf(offer?.startDate ?: Date())
    var endDate by mutableStateOf(
        if (offer != null) {
            Date.from(
                offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
            )
        } else {
            Date()
        }
    )
    var beneficiaries by mutableStateOf(offer?.beneficiaries ?: 0)
    var price by mutableStateOf(offer?.price ?: 0.0)
    var locationId by mutableStateOf(offer?.location?.id ?: "")
    var products by mutableStateOf(emptyList<ProductCreateRequest>())

    init {
        fetchPublishedLocations()
    }

    private fun fetchPublishedLocations() {
        locationsResponse = ApiStatus.LOADING
        viewModelScope.launch {
            val response =
                locationServiceWrapper.getLocations(token.bearerToken, locationPaginationQuery)
            if (response.isSuccessful) {
                val responsePage = response.body()!!
                if (responsePage.hasNext) {
                    locations = locations.plus(responsePage.items)
                    locationPaginationQuery = locationPaginationQuery++
                    fetchPublishedLocations()
                } else {
                    locations = locations.plus(responsePage.items)
                }
            } else {
                locationsResponse = ApiStatus.ERROR
            }
        }
    }


}