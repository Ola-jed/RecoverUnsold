package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent

@Composable
fun OfferDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offerId: String
) {
    // TODO
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
    ) {}
}