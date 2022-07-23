package com.ola.recoverunsold.ui.screens.distributor.orders

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.screens.viewmodels.OrdersReceivedViewModel

@Composable
fun DistributorOrdersReceivedScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    ordersReceivedViewModel: OrdersReceivedViewModel = viewModel()
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
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { paddingValues ->
        when (ordersReceivedViewModel.ordersGetResponse.status) {

        }
    }
}