package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.screens.distributor.home.DistributorHomeScreen
import com.ola.recoverunsold.utils.store.UserObserver

@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val user by UserObserver.user.collectAsState()

    return when (user) {
        null -> CustomerHomeScreen(navController, snackbarHostState)
        is Customer -> CustomerHomeScreen(navController, snackbarHostState)
        is Distributor -> DistributorHomeScreen(navController, snackbarHostState)
    }
}