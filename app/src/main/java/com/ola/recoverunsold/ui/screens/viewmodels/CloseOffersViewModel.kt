package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.query.OfferDistanceFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class CloseOffersViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = KoinJavaComponent.get(OfferServiceWrapper::class.java)
) : ViewModel() {
    var formState by mutableStateOf(FormState())
    var offerDistanceFilterQuery by mutableStateOf(
        OfferDistanceFilterQuery(
            distance = 1.0,
            latLong = LatLong.zero()
        )
    )
    var closeOffersApiResult: ApiCallResult<Page<OfferWithRelativeDistance>> by mutableStateOf(ApiCallResult.Inactive)

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
}