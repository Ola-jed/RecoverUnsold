package com.ola.recoverunsold.ui.screens.customer

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.CustomerUpdateRequest
import com.ola.recoverunsold.api.requests.DistributorUpdateRequest
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.DrawerContent
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun CustomerAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    accountServiceViewModel: ViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val scrollState = rememberScrollState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) {

    }
}

class CustomerAccountViewModel(
    private val accountService: AccountService = get(
        AccountService::class.java
    )
) : ViewModel() {
    private val customer = (UserObserver.user.value!! as Customer)
    private val token = TokenStore.get()!!

    var usernameTextField by mutableStateOf(customer.username)
    var firstNameTextField by mutableStateOf(customer.firstName ?: "")
    var lastNameTextField by mutableStateOf(customer.lastName ?: "")

    var username by mutableStateOf(customer.username)
    var firstName by mutableStateOf(customer.firstName ?: "")
    var lastName by mutableStateOf(customer.lastName ?: "")

    var accountApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())

    fun updateCustomer() {
        if (username.isBlank()) return
        accountApiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = accountService.updateCustomer(token.bearerToken,
                CustomerUpdateRequest(
                    username,
                    firstName.ifBlank { null },
                    lastName.ifBlank { null }
                )
            )
            accountApiCallResult = if (response.isSuccessful) {
                UserObserver.update((UserObserver.user.value as Customer).copy(
                    username = username,
                    firstName = firstName.ifBlank { null },
                    lastName = lastName.ifBlank { null }
                ))
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun deleteCustomer(onDeleteSuccess: () -> Unit) {
        accountApiCallResult = ApiCallResult.Loading()
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