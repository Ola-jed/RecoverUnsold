package com.ola.recoverunsold.ui.screens.distributor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.DistributorInformationComponent
import com.ola.recoverunsold.ui.components.DrawerContent
import com.ola.recoverunsold.utils.store.UserObserver

// TODO : Create viewModel and finish
@Composable
fun DistributorAccountScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
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
                .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
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

@Composable
fun DistributorProfileInformationSection(
    distributor: Distributor,
    isEditing: Boolean,
    onEditingStart: () -> Unit,
    onEditingEnd: () -> Unit,
    onEditingCancel: () -> Unit,
    loading: Boolean
) {
    Text(
        stringResource(R.string.profile_information_label),
        modifier = Modifier
            .padding(vertical = 10.dp),
        fontSize = 17.sp
    )
    if (loading) {
        CircularProgressIndicator(color = MaterialTheme.colors.background)
    } else {
        if (isEditing) {
            Column {
                Text("You are editing this data")
                Row {
                    TextButton(onClick = onEditingEnd) {
                        Text(stringResource(R.string.save))
                    }
                    TextButton(onClick = onEditingCancel) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        } else {
            Column {
                DistributorInformationComponent(distributor = distributor)
                TextButton(onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }
            }
        }
    }
}