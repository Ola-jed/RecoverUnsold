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
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.validation.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerAccountViewModel @Inject constructor(
    private val accountService: AccountService
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
            accountApiCallResult = accountService.updateCustomer(
                token.bearerToken,
                CustomerUpdateRequest(username, firstName.nullIfBlank(), lastName.nullIfBlank())
            ).toApiCallResult {
                UserObserver.update(
                    (UserObserver.user.value as Customer).copy(
                        username = username,
                        firstName = firstName.nullIfBlank(),
                        lastName = lastName.nullIfBlank()
                    )
                )
            }
        }
    }

    fun deleteCustomer(onDeleteSuccess: () -> Unit) {
        accountApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            accountApiCallResult = accountService
                .deleteAccount(token.bearerToken)
                .toApiCallResult(onDeleteSuccess)
        }
    }

    fun errorMessage(): String? = when (accountApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}