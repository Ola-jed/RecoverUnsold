package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.DrawerContent
import com.ola.recoverunsold.utils.store.UserObserver

// TODO : Create viewModel and finish
@Composable
fun DistributorAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorAccountViewModel: DistributorAccountViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val scrollState = rememberScrollState()
    val user by UserObserver.user.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DistributorProfileInformationSection(
                distributor = user!! as Distributor,
                isEditing = isEditing,
                onEditingStart = { isEditing = true },
                onEditingEnd = { isEditing = false },
                onEditingCancel = { isEditing = false },
                loading = false
            )
        }
    }
}

class DistributorAccountViewModel() : ViewModel() {
    var accountUpdateApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())


}