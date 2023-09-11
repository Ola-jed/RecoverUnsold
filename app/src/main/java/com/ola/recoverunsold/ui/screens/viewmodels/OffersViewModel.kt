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
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val offerServiceWrapper: OfferServiceWrapper
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery(active = true))
    var offersApiResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive)

    init {
        getOffers()
    }

    fun getOffers() {
        offersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            offersApiResult = offerServiceWrapper
                .getOffers(offerFilterQuery)
                .toApiCallResult()
        }
    }

    fun loadMore() {
        offerFilterQuery = offerFilterQuery.inc()
        val savedPage = offersApiResult.data!!
        offersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            val extraApiCallResponse = offerServiceWrapper
                .getOffers(offerFilterQuery)
                .toApiCallResult()

            if (extraApiCallResponse is ApiCallResult.Error) {
                offersApiResult = extraApiCallResponse
            } else if (extraApiCallResponse is ApiCallResult.Success) {
                offersApiResult = extraApiCallResponse.copy(
                    _data = extraApiCallResponse.data!!.prepend(savedPage)
                )
            }
        }
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