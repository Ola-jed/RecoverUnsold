package com.ola.recoverunsold.ui.screens.customer

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.components.account.CustomerProfileInformationSection
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.CustomerAccountViewModel
import com.ola.recoverunsold.utils.misc.logout
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch

@Composable
fun CustomerAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    customerAccountServiceViewModel: CustomerAccountViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val user by UserObserver.user.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

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
        CustomerProfileInformationSection(
            customer = user!! as Customer,
            username = customerAccountServiceViewModel.username,
            firstName = customerAccountServiceViewModel.firstName,
            lastName = customerAccountServiceViewModel.lastName,
            isEditing = isEditing,
            onEditingStart = { isEditing = true },
            onEditingEnd = {
                if (!customerAccountServiceViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = customerAccountServiceViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    customerAccountServiceViewModel.updateCustomer()
                    isEditing = false
                }
            },
            onEditingCancel = { isEditing = false },
            loading = customerAccountServiceViewModel.accountApiCallResult.status == ApiStatus.LOADING,
            onUsernameChange = { customerAccountServiceViewModel.username = it },
            onFirstNameChange = { customerAccountServiceViewModel.firstName = it },
            onLastNameChange = { customerAccountServiceViewModel.lastName = it },
            onDelete = {
                customerAccountServiceViewModel.deleteCustomer {
                    coroutineScope.launch {
                        context.logout()
                        navController.navigate(Routes.Home.path) {
                            popUpTo(Routes.Home.path) { inclusive = true }
                        }
                        snackbarHostState.show(
                            message = Strings.get(R.string.account_deleted_successfully)
                        )
                    }
                }
            },
            onValidationSuccess = {
                customerAccountServiceViewModel.formState =
                    customerAccountServiceViewModel.formState
                        .copy(
                            isValid = true,
                            errorMessage = null
                        )
            },
            onValidationError = {
                customerAccountServiceViewModel.formState =
                    customerAccountServiceViewModel.formState.copy(
                        isValid = false,
                        errorMessage = it
                    )
            }
        )
    }
}