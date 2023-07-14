package com.ola.recoverunsold.ui.screens.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.components.account.CustomerProfileInformationSection
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.CustomerAccountViewModel
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch

@Composable
fun CustomerAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    customerAccountServiceViewModel: CustomerAccountViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val user by UserObserver.user.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.account),
                        actions = {
                            IconButton(onClick = { navController.navigate(Routes.Alerts.path) }) {
                                Icon(
                                    modifier = Modifier.padding(start = 5.dp),
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (user != null) {
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
                                    customerAccountServiceViewModel
                                        .formState
                                        .copy(isValid = true, errorMessage = null)
                            },
                            onValidationError = {
                                customerAccountServiceViewModel.formState =
                                    customerAccountServiceViewModel
                                        .formState
                                        .copy(isValid = false, errorMessage = it)
                            }
                        )
                    }
                }
            }
        }
    )
}