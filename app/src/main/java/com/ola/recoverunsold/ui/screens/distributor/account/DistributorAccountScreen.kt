package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.account.DistributorProfileInformationSection
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorAccountViewModel
import com.ola.recoverunsold.utils.misc.logout
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch

@Composable
fun DistributorAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorAccountViewModel: DistributorAccountViewModel = hiltViewModel()
) {
    val profileIndex = 0
    val locationsIndex = 1

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val user by UserObserver.user.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabTitles = listOf(stringResource(R.string.information), stringResource(R.string.locations))
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.account)
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .run { if (tabIndex == profileIndex) verticalScroll(scrollState) else this },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .clip(RoundedCornerShape(50)),
                        indicator = { Box {} }
                    ) {
                        tabTitles.forEachIndexed { index, text ->
                            val selected = tabIndex == index

                            Tab(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (selected) {
                                            MaterialTheme.colorScheme.background
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    ),
                                selected = selected,
                                onClick = { tabIndex = index },
                                text = {
                                    Text(
                                        text = text,
                                        color = if (selected) {
                                            MaterialTheme.colorScheme.onBackground
                                        } else {
                                            MaterialTheme.colorScheme.onPrimary
                                        }
                                    )
                                }
                            )
                        }
                    }
                    when (tabIndex) {
                        profileIndex -> DistributorProfileInformationSection(
                            distributor = user!! as Distributor,
                            username = distributorAccountViewModel.username,
                            phone = distributorAccountViewModel.phone,
                            rccm = distributorAccountViewModel.rccm,
                            taxId = distributorAccountViewModel.taxId,
                            websiteUrl = distributorAccountViewModel.websiteUrl,
                            isEditing = isEditing,
                            onEditingStart = { isEditing = true },
                            onEditingEnd = {
                                if (!distributorAccountViewModel.formState.isValid) {
                                    coroutineScope.launch {
                                        snackbarHostState.show(
                                            message = distributorAccountViewModel.formState.errorMessage
                                                ?: Strings.get(R.string.invalid_data)
                                        )
                                    }
                                } else {
                                    distributorAccountViewModel.updateDistributor()
                                    isEditing = false
                                }
                            },
                            onEditingCancel = { isEditing = false },
                            loading = distributorAccountViewModel.accountApiCallResult.status == ApiStatus.LOADING,
                            onUsernameChange = { distributorAccountViewModel.username = it },
                            onPhoneChange = { distributorAccountViewModel.phone = it },
                            onRccmChange = { distributorAccountViewModel.rccm = it },
                            onTaxIdChange = { distributorAccountViewModel.taxId = it },
                            onWebsiteUrlChange = { distributorAccountViewModel.websiteUrl = it },
                            onDelete = {
                                distributorAccountViewModel.deleteDistributor {
                                    coroutineScope.launch {
                                        context.logout()
                                        navController.navigate(Routes.Home.path) {
                                            popUpTo(Routes.Home.path) { inclusive = true }
                                        }
                                        snackbarHostState.show(Strings.get(R.string.account_deleted_successfully))
                                    }
                                }
                            },
                            onValidationSuccess = {
                                distributorAccountViewModel.formState =
                                    distributorAccountViewModel.formState
                                        .copy(
                                            isValid = true,
                                            errorMessage = null
                                        )
                            },
                            onValidationError = {
                                distributorAccountViewModel.formState =
                                    distributorAccountViewModel.formState.copy(
                                        isValid = false,
                                        errorMessage = it
                                    )
                            }
                        )

                        locationsIndex -> DistributorLocationsScreen(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            snackbarHostState = snackbarHostState
                        )

                        else -> Box(Modifier)
                    }

                    if (distributorAccountViewModel.accountApiCallResult.status == ApiStatus.SUCCESS) {
                        LaunchedEffect(snackbarHostState) {
                            coroutineScope.launch {
                                snackbarHostState.show(Strings.get(R.string.account_updated_successfully))
                            }
                        }
                    }

                    if (distributorAccountViewModel.errorMessage() != null) {
                        LaunchedEffect(snackbarHostState) {
                            coroutineScope.launch {
                                snackbarHostState.show(distributorAccountViewModel.errorMessage()!!)
                            }
                        }
                    }
                }
            }
        }
    )
}