package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.utils.store.UserObserver

@Composable
fun DrawerContent(navController: NavController): @Composable ColumnScope.() -> Unit {
    val user by UserObserver.user.collectAsState()

    return when (user) {
        null -> VisitorDrawer(navController = navController)
        is Customer -> CustomerDrawer(user as Customer, navController)
        is Distributor -> DistributorDrawer(user as Distributor, navController)
    }
}