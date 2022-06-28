package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.runBlocking

@Composable
fun DrawerContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState
): @Composable ColumnScope.() -> Unit {
    val user by UserObserver.user.collectAsState(runBlocking {
        UserObserver.user.value
    })
    return when (user) {
        null -> VisitorDrawer(navController = navController)
        else -> {
            AuthDrawer(
                user = user!!,
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

