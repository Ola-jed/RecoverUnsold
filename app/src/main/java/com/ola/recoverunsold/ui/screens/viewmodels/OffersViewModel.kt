package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class OffersViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = KoinJavaComponent.get(OfferServiceWrapper::class.java)
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery(active = true))
    var offersApiResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive)

    init {
        getOffers()
    }

    fun getOffers() {
        offersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = offerServiceWrapper.getOffers(offerFilterQuery)
            offersApiResult = if (response.isSuccessful) {
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

    fun resetFilter() {
        offerFilterQuery = OfferFilterQuery(active = true)
    }

    fun errorMessage(): String? {
        if (offersApiResult.status == ApiStatus.ERROR) {
            return when (offersApiResult.statusCode) {
                StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
                else -> Strings.get(R.string.unknown_error_occured)
            }
        }
        return null
    }
}