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
import com.ola.recoverunsold.api.requests.ReportCreateRequest
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.api.services.ReportsService
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.extensions.nullIfBlank
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DistributorDetailsViewModel @AssistedInject constructor(
    private val distributorService: DistributorService,
    private val reportsService: ReportsService,
    private val offerServiceWrapper: OfferServiceWrapper,
    @Assisted private val distributorId: String
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery())
    var distributorApiCallResult: ApiCallResult<DistributorInformation> by mutableStateOf(
        ApiCallResult.Inactive
    )
    var offersApiCallResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive)
    var isAuthenticated by mutableStateOf(UserObserver.user.value != null)
    var reportingDistributor by mutableStateOf(false)
    var reportReason by mutableStateOf("")
    var reportMessage by mutableStateOf("")

    init {
        getDistributorInformation()
    }

    fun getDistributorInformation() {
        distributorApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            distributorApiCallResult = distributorService
                .getDistributor(distributorId)
                .toApiCallResult()
            getOffers()
        }
    }

    fun getOffers() {
        offersApiCallResult = ApiCallResult.Loading
        if (distributorApiCallResult.data != null) {
            viewModelScope.launch {
                offersApiCallResult = offerServiceWrapper
                    .getDistributorOffers(distributorApiCallResult.data!!.id, offerFilterQuery)
                    .toApiCallResult()
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

    fun reportDistributor(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            reportingDistributor = true
            val response = reportsService.reportDistributor(
                distributorId,
                ReportCreateRequest(reportReason, reportMessage.nullIfBlank())
            ).toApiCallResult()

            reportingDistributor = false
            if (response.statusCode <= 400) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(distributorId: String): DistributorDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            distributorDetailsViewModelFactory: Factory,
            distributorId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return distributorDetailsViewModelFactory.create(distributorId) as T
            }
        }
    }
}