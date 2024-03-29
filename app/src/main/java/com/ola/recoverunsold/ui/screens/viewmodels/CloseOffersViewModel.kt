package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.OfferDistanceFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloseOffersViewModel @Inject constructor(
    private val offerServiceWrapper: OfferServiceWrapper
) : ViewModel() {
    var formState by mutableStateOf(FormState(isValid = true))
    var offerDistanceFilterQuery by mutableStateOf(
        OfferDistanceFilterQuery(
            distance = 1.0,
            latLong = LatLong.zero
        )
    )
    var closeOffersApiResult: ApiCallResult<Page<OfferWithRelativeDistance>> by mutableStateOf(
        ApiCallResult.Inactive
    )

    fun getCloseOffers() {
        closeOffersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            closeOffersApiResult = offerServiceWrapper
                .getCloseOffers(offerDistanceFilterQuery)
                .toApiCallResult()
        }
    }

    fun loadMore() {
        offerDistanceFilterQuery = offerDistanceFilterQuery.nextPage()
        val savedPage = closeOffersApiResult.data!!
        closeOffersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            val extraApiCallResponse = offerServiceWrapper
                .getCloseOffers(offerDistanceFilterQuery)
                .toApiCallResult()

            if (extraApiCallResponse is ApiCallResult.Error) {
                closeOffersApiResult = extraApiCallResponse
            } else if (extraApiCallResponse is ApiCallResult.Success) {
                closeOffersApiResult = extraApiCallResponse.copy(
                    _data = extraApiCallResponse.data!!.prepend(savedPage)
                )
            }
        }
    }

    fun errorMessage(): String? = when (closeOffersApiResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}