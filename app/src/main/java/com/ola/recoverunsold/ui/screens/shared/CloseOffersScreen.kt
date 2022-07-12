package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.screens.viewmodels.CloseOffersViewModel
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros

@Composable
fun CloseOffersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    closeOffersViewModel: CloseOffersViewModel = viewModel()
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                // TODO
            }
        }
    }
}

@Composable
fun CloseOffersFormContent(
    modifier: Modifier = Modifier,
    distance: Double,
    onDistanceUpdate: (Double) -> Unit,
    onDistanceValidationError: (String) -> Unit,
    onDistanceValidationSuccess: () -> Unit
) {
    Column(modifier = modifier) {
        CustomTextInput(
            value = distance.formatWithoutTrailingZeros(),
            onValueChange = {},
            label = { Text(text = stringResource(R.string.minimum_price_label)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )
    }
}