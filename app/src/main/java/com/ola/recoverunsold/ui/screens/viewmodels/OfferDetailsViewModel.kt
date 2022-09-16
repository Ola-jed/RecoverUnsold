package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.OrderCreateRequest
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.ProductServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.Date

class OfferDetailsViewModel @AssistedInject constructor(
    private val offerServiceWrapper: OfferServiceWrapper,
    private val productServiceWrapper: ProductServiceWrapper,
    private val orderService: OrderServiceWrapper,
    @Assisted private val offerId: String
) : ViewModel() {
    var offerApiCallResult: ApiCallResult<Offer> by mutableStateOf(ApiCallResult.Inactive)
    var orderApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var withdrawalDate by mutableStateOf(Date())
    val currentUserId = UserObserver.user.value?.id ?: ""
    val token = TokenStore.get()?.bearerToken ?: ""
    val isCustomer = TokenStore.get()?.role == TokenRoles.CUSTOMER
    val isDistributor = TokenStore.get()?.role == TokenRoles.DISTRIBUTOR

    init {
        getOffer()
    }

    fun getOffer() {
        offerApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            offerApiCallResult = offerServiceWrapper
                .getOffer(offerId)
                .toApiCallResult()
        }
    }

    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val response = productServiceWrapper.deleteProduct(product.id, token)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    fun orderProduct() {
        orderApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = orderService.createOrder(
                token,
                offerId,
                OrderCreateRequest(withdrawalDate)
            )
            orderApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (offerApiCallResult.statusCode) {
        StatusCode.NotFound.code -> Strings.get(R.string.offer_not_found)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }

    fun orderErrorMessage(): String? = when (orderApiCallResult.statusCode) {
        StatusCode.NotFound.code -> Strings.get(R.string.offer_not_found)
        StatusCode.Forbidden.code -> Strings.get(R.string.beneficiaries_number_already_reached)
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_date)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }

    @AssistedFactory
    interface Factory {
        fun create(offerId: String): OfferDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            offerDetailsViewModelFactory: Factory,
            offerId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return offerDetailsViewModelFactory.create(offerId) as T
            }
        }
    }
}

