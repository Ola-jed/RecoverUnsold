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
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val reviewsService: ReviewsService) : ViewModel() {
    private val token = TokenStore.get()?.bearerToken
    val isAuthenticated = TokenStore.get() != null
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var message by mutableStateOf("")

    fun publishMessage() {
        if (token != null) {
            apiCallResult = ApiCallResult.Loading
            viewModelScope.launch {
                apiCallResult = reviewsService
                    .publishReview(ReviewMessageRequest(message))
                    .toApiCallResult()
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