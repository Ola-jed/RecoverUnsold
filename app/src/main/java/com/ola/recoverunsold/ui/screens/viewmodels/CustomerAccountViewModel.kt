package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.CustomerUpdateRequest
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.utils.misc.nullIfBlank
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class CustomerAccountViewModel(
    private val accountService: AccountService = KoinJavaComponent.get(
        AccountService::class.java
    )
) : ViewModel() {
    private val customer = (UserObserver.user.value!! as Customer)
    private val token = TokenStore.get()!!
    var username by mutableStateOf(customer.username)
    var firstName by mutableStateOf(customer.firstName ?: "")
    var lastName by mutableStateOf(customer.lastName ?: "")
    var accountApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var formState by mutableStateOf(FormState())

    fun updateCustomer() {
        accountApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = accountService.updateCustomer(
                token.bearerToken,
                CustomerUpdateRequest(
                    username,
                    firstName.nullIfBlank(),
                    lastName.nullIfBlank()
                )
            )
            accountApiCallResult = if (response.isSuccessful) {
                UserObserver.update(
                    (UserObserver.user.value as Customer).copy(
                        username = username,
                        firstName = firstName.nullIfBlank(),
                        lastName = lastName.nullIfBlank()
                    )
                )
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun deleteCustomer(onDeleteSuccess: () -> Unit) {
        accountApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = accountService.deleteAccount(token.bearerToken)
            accountApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit).also {
                    onDeleteSuccess()
                }
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (accountApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}