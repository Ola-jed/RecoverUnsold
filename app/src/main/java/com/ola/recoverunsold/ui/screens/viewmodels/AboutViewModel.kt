package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.ReviewMessageRequest
import com.ola.recoverunsold.api.services.ReviewsService
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class AboutViewModel(
    private val reviewsService: ReviewsService = KoinJavaComponent.get(ReviewsService::class.java)
) : ViewModel() {
    private val token: String = TokenStore.get()!!.bearerToken
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var message by mutableStateOf("")

    fun publishMessage() {
        apiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = reviewsService.publishReview(
                token,
                ReviewMessageRequest(message)
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? {
        return when (apiCallResult.statusCode) {
            in 400..600 -> Strings.get(R.string.failed_to_send_message)
            else -> null
        }
    }
}