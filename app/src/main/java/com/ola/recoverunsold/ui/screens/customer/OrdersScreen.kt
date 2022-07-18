package com.ola.recoverunsold.ui.screens.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.order.OrderFilterComponent
import com.ola.recoverunsold.ui.components.order.OrderItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.CustomerOrderViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    customerOrderViewModel: CustomerOrderViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { paddingValues ->
        when (customerOrderViewModel.ordersGetResponse.status) {
            ApiStatus.LOADING, ApiStatus.INACTIVE -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                            customerOrderViewModel.resetFilters()
                            customerOrderViewModel.getOrders()
                        },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(stringResource(id = R.string.retry))
                    }
                }

                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = customerOrderViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val orders = customerOrderViewModel.ordersGetResponse.data!!

                Column(modifier = Modifier.padding(paddingValues)) {
                    OrderFilterComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        orderStatus = customerOrderViewModel.orderQuery.status?.let {
                            OrderStatus.valueOf(it)
                        },
                        onOrderStatusChange = {
                            customerOrderViewModel.orderQuery = customerOrderViewModel
                                .orderQuery
                                .copy(status = it?.name)
                            customerOrderViewModel.getOrders()
                        }
                    )

                    if (orders.items.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                stringResource(R.string.no_order_found),
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            items(items = orders.items) {
                                OrderItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                    order = it,
                                    onMoreInformationRequest = {
                                        navController.navigate(
                                            Routes.OfferDetails.path.replace(
                                                "{offerId}",
                                                it.offerId
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (orders.pageNumber > 1) {
                                        Button(onClick = { customerOrderViewModel.getPrevious() }) {
                                            Text(stringResource(id = R.string.previous))
                                        }
                                    }
                                    if (orders.hasNext) {
                                        Button(onClick = { customerOrderViewModel.getNext() }) {
                                            Text(stringResource(id = R.string.next))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}