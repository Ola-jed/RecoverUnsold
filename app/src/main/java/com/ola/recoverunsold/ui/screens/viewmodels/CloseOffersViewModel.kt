package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.query.OfferDistanceFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import org.koin.java.KoinJavaComponent

class CloseOffersViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = KoinJavaComponent.get(OfferServiceWrapper::class.java)
) : ViewModel() {
    var offerDistanceFilterQuery by mutableStateOf(
        OfferDistanceFilterQuery(
            distance = 1.0,
            latitude = 0.0,
            longitude = 0.0
        )
    )

    var closeOffersApiResult: ApiCallResult<Page<Offer>>by mutableStateOf(ApiCallResult.Inactive)


    fun getCloseOffers() {
        closeOffersApiResult = ApiCallResult.Loading()
    }
}