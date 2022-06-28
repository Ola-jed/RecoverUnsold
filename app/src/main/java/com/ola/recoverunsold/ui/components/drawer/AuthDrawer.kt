package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.User

@Composable
fun AuthDrawer(
    user: User,
    navController: NavController,
    snackbarHostState: SnackbarHostState
): @Composable ColumnScope.() -> Unit {
    return when (user) {
        is Customer -> CustomerDrawer(user, navController, snackbarHostState)
        is Distributor -> DistributorDrawer(user, navController, snackbarHostState)
    }
}