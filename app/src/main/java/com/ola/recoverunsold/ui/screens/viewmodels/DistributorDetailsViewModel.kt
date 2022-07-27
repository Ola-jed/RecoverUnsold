package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class DistributorDetailsViewModelFactory(private val distributorId: String) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DistributorDetailsViewModel(distributorId = distributorId) as T
    }
}

class DistributorDetailsViewModel(
    private val distributorId: String,
    private val distributorService: DistributorService = KoinJavaComponent.get(
        DistributorService::class.java
    ),
    private val offerServiceWrapper: OfferServiceWrapper = KoinJavaComponent.get(
        OfferServiceWrapper::class.java
    )
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery())
    var distributorApiCallResult: ApiCallResult<DistributorInformation> by mutableStateOf(
        ApiCallResult.Inactive
    )
    var offersApiCallResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive)

    init {
        getDistributorInformation()
        getOffers()
    }

    fun getDistributorInformation() {
        distributorApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = distributorService.getDistributor(distributorId)
            distributorApiCallResult = if(response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getOffers() {
        offersApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = offerServiceWrapper.getOffers(offerFilterQuery)
            offersApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getNext() {
        offerFilterQuery = offerFilterQuery.inc()
        getOffers()
    }

    fun getPrevious() {
        offerFilterQuery = offerFilterQuery.dec()
        getOffers()
    }

    fun errorMessage(): String? {
        return if (distributorApiCallResult.status == ApiStatus.ERROR
            || offersApiCallResult.status == ApiStatus.ERROR
        ) {
            Strings.get(R.string.unknown_error_occured)
        } else {
            null
        }
    }
}