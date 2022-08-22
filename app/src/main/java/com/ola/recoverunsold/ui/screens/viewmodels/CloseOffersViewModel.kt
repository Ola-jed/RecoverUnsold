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
            latLong = LatLong.zero()
        )
    )
    var closeOffersApiResult: ApiCallResult<Page<OfferWithRelativeDistance>> by mutableStateOf(
        ApiCallResult.Inactive
    )

    fun getCloseOffers() {
        closeOffersApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = offerServiceWrapper.getCloseOffers(offerDistanceFilterQuery)
            closeOffersApiResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getPrevious() {
        offerDistanceFilterQuery = offerDistanceFilterQuery.previousPage()
        getCloseOffers()
    }

    fun getNext() {
        offerDistanceFilterQuery = offerDistanceFilterQuery.nextPage()
        getCloseOffers()
    }

    fun errorMessage(): String? = when (closeOffersApiResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}