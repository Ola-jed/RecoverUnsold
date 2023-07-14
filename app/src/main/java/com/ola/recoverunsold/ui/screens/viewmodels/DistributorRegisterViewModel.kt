package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.DistributorRegisterRequest
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.utils.extensions.nullIfBlank
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistributorRegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userVerificationService: UserVerificationService
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var taxId by mutableStateOf("")
    var rccm by mutableStateOf("")
    var websiteUrl by mutableStateOf("")
    var formState by mutableStateOf(FormState())

    fun register() {
        apiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = authService.registerDistributor(
                DistributorRegisterRequest(
                    email,
                    taxId,
                    password,
                    phone,
                    rccm,
                    username,
                    websiteUrl.nullIfBlank()
                )
            )
            apiCallResult = if (response.isSuccessful) {
                val userVerificationResponse = userVerificationService.startUserVerification(
                    UserVerificationStartRequest(email)
                )
                if (userVerificationResponse.isSuccessful) {
                    ApiCallResult.Success(Unit)
                } else {
                    ApiCallResult.Error(code = StatusCode.Unknown.code)
                }
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        StatusCode.Unknown.code -> Strings.get(R.string.register_success_verification_failed)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}