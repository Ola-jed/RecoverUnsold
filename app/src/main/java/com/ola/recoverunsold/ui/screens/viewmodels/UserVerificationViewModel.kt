package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.UserVerificationConfirmRequest
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class UserVerificationViewModel(
    private val userVerificationService: UserVerificationService = KoinJavaComponent.get(
        UserVerificationService::class.java
    )
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var token by mutableStateOf("")
    var formState by mutableStateOf(FormState())

    fun submit() {
        apiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = userVerificationService.confirmUserVerification(
                UserVerificationConfirmRequest(token)
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_expired_code)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}