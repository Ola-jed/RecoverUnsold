package com.ola.recoverunsold.ui.screens.customer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.ui.components.alerts.AlertForm
import com.ola.recoverunsold.ui.components.alerts.AlertItem
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.screens.viewmodels.AlertsViewModel
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    alertsViewModel: AlertsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = true,
            initialValue = SheetValue.Hidden
        ),
        snackbarHostState = snackbarHostState
    )
    val listState = rememberLazyListState()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.my_alerts),
                        canGoBack = true,
                        navController = navController,
                        actions = {
                            IconButton(onClick = {
                                coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                            }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(id = R.string.add)
                                )
                            }
                        }
                    )
                },
                sheetContent = {
                    AlertForm(
                        modifier = Modifier
                            .fillMaxWidth(0.8F)
                            .padding(vertical = 25.dp)
                            .align(Alignment.CenterHorizontally),
                        alertType = alertsViewModel.alertType,
                        onAlertTypeChange = { alertsViewModel.alertType = it },
                        distributorLabel = alertsViewModel.distributorLabel,
                        distributorLabels = alertsViewModel.distributorsLabels,
                        onDistributorLabelChange = { alertsViewModel.distributorLabel = it },
                        onSubmit = {
                            alertsViewModel.createAlert(
                                onSuccess = {
                                    coroutineScope.launch {
                                        snackbarHostState.show(Strings.get(R.string.alert_created_successfully))
                                        bottomSheetScaffoldState.bottomSheetState.hide()
                                    }

                                    alertsViewModel.getAlerts()
                                },
                                onFailure = {
                                    val message = when (it) {
                                        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
                                        else -> Strings.get(R.string.alert_creation_failed)
                                    }

                                    coroutineScope.launch { snackbarHostState.show(message) }
                                }
                            )
                        },
                    )
                },
                sheetPeekHeight = 0.dp,
                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            ) { paddingValues ->
                when (alertsViewModel.alertsApiCallResult.status) {
                    ApiStatus.INACTIVE, ApiStatus.LOADING -> LoadingIndicator(
                        modifier = Modifier.padding(paddingValues)
                    )

                    ApiStatus.ERROR -> {
                        Box(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            Button(
                                onClick = { alertsViewModel.getAlerts() },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(stringResource(id = R.string.retry))
                            }
                        }

                        LaunchedEffect(snackbarHostState) {
                            coroutineScope.launch {
                                snackbarHostState.show(
                                    message = alertsViewModel.errorMessage()
                                        ?: Strings.get(R.string.unknown_error_occured)
                                )
                            }
                        }
                    }

                    else -> {
                        val alerts = alertsViewModel.alertsApiCallResult.data!!

                        if (alerts.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.no_alerts_defined),
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize(),
                                state = listState
                            ) {
                                items(items = alerts) {
                                    AlertItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 10.dp),
                                        alert = it,
                                        onDelete = {
                                            alertsViewModel.deleteAlert(
                                                alert = it,
                                                onSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(
                                                            Strings.get(R.string.alert_successfully_deleted)
                                                        )
                                                    }
                                                    alertsViewModel.getAlerts()
                                                },
                                                onFailure = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(
                                                            Strings.get(R.string.alert_deletion_failed)
                                                        )
                                                    }
                                                    alertsViewModel.getAlerts()
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}