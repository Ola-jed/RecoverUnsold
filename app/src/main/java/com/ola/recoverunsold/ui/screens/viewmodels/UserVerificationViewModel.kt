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
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVerificationViewModel @Inject constructor(
    private val userVerificationService: UserVerificationService
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var token by mutableStateOf("")
    var formState by mutableStateOf(FormState())

    fun submit() {
        apiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            apiCallResult = userVerificationService
                .confirmUserVerification(UserVerificationConfirmRequest(token))
                .toApiCallResult()
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_expired_code)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}