package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.LoginRequest
import com.ola.recoverunsold.api.responses.Token
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val accountService: AccountService,
    private val fcmTokenService: FcmTokenService
) : ViewModel() {
    var apiCallResult: ApiCallResult<Token> by mutableStateOf(ApiCallResult.Inactive)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var formState by mutableStateOf(FormState())

    fun login(onSuccess: () -> Unit = {}) {
        apiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = authService.login(LoginRequest(email = email, password = password))
            apiCallResult = if (response.isSuccessful) {
                val token = response.body()
                ApiCallResult.Success(_data = token)
            } else {
                ApiCallResult.Error(code = response.code())
            }
            if (apiCallResult.status == ApiStatus.SUCCESS) {
                onSuccess()
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.Unauthorized.code -> Strings.get(R.string.invalid_credentials)
        StatusCode.Forbidden.code -> Strings.get(R.string.account_not_verified)
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }

    fun accountService(): AccountService = accountService
    fun fcmTokenService(): FcmTokenService = fcmTokenService
}