package com.ola.recoverunsold.ui.screens.distributor.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.order.DistributorOrderItem
import com.ola.recoverunsold.ui.components.order.OrderFilterComponent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.OrdersReceivedViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DistributorOrdersReceivedScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    ordersReceivedViewModel: OrdersReceivedViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                title = stringResource(id = R.string.orders_received)
            )
        },
        drawerContent = DrawerContent(navController)
    ) { paddingValues ->
        when (ordersReceivedViewModel.ordersGetResponse.status) {
            ApiStatus.LOADING, ApiStatus.INACTIVE -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                            ordersReceivedViewModel.resetFilters()
                            ordersReceivedViewModel.getOrders()
                        },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(stringResource(id = R.string.retry))
                    }
                }

                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = ordersReceivedViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val orders = ordersReceivedViewModel.ordersGetResponse.data!!

                Column(modifier = Modifier.padding(paddingValues)) {
                    OrderFilterComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        orderStatus = ordersReceivedViewModel.orderQuery.status?.let {
                            OrderStatus.valueOf(it)
                        },
                        onOrderStatusChange = {
                            ordersReceivedViewModel.orderQuery = ordersReceivedViewModel
                                .orderQuery
                                .copy(status = it?.name, page = 1)
                            ordersReceivedViewModel.getOrders()
                        }
                    )

                    if (orders.items.isEmpty()) {
                        NoContentComponent(
                            modifier = Modifier.fillMaxWidth(),
                            message = stringResource(R.string.no_order_found)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            items(items = orders.items) {
                                DistributorOrderItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp, vertical = 20.dp),
                                    order = it,
                                    onMoreInformationRequest = {
                                        navController.navigate(
                                            Routes.OrderDetails
                                                .path
                                                .replace("{orderId}", it.id)
                                        )
                                    },
                                    onOrderAccept = {
                                        ordersReceivedViewModel.acceptOrder(
                                            order = it,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.order_successfully_accepted)
                                                    )
                                                    delay(7000)
                                                }
                                            },
                                            onFailure = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.unknown_error_occured)
                                                    )
                                                    delay(7000)
                                                }
                                            }
                                        )
                                        ordersReceivedViewModel.getOrders()
                                    },
                                    onOrderReject = {
                                        ordersReceivedViewModel.rejectOrder(
                                            order = it,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.order_successfully_rejected)
                                                    )
                                                    delay(7000)
                                                }
                                            },
                                            onFailure = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.unknown_error_occured)
                                                    )
                                                    delay(7000)
                                                }
                                            }
                                        )
                                        ordersReceivedViewModel.getOrders()
                                    },
                                    onOrderComplete = {
                                        ordersReceivedViewModel.completeOrder(
                                            order = it,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.order_completed_successfully)
                                                    )
                                                    delay(7000)
                                                }
                                            },
                                            onFailure = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.unknown_error_occured)
                                                    )
                                                    delay(7000)
                                                }
                                            }
                                        )
                                        ordersReceivedViewModel.getOrders()
                                    }
                                )
                            }

                            item {
                                PaginationComponent(
                                    modifier = Modifier.fillMaxWidth(),
                                    page = orders,
                                    onPrevious = { ordersReceivedViewModel.getPrevious() },
                                    onNext = { ordersReceivedViewModel.getNext() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}