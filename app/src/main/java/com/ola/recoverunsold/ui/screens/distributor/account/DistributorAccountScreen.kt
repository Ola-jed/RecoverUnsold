package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.requests.DistributorUpdateRequest
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.DistributorProfileInformationSection
import com.ola.recoverunsold.ui.components.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.logout
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun DistributorAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorAccountViewModel: DistributorAccountViewModel = viewModel(),
) {
    val profileIndex = 0
    val locationsIndex = 1

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val scrollState = rememberScrollState()
    val user by UserObserver.user.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabTitles = listOf(stringResource(R.string.information), stringResource(R.string.locations))
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .run {
                    if (tabIndex == profileIndex) {
                        verticalScroll(scrollState)
                    } else {
                        this
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(selectedTabIndex = tabIndex,
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .clip(RoundedCornerShape(50)),
                indicator = {
                    Box {}
                }
            ) {
                tabTitles.forEachIndexed { index, text ->
                    val selected = tabIndex == index
                    Tab(
                        modifier = if (selected) {
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colors.background)
                        } else {
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colors.primary)
                        },
                        selected = selected,
                        onClick = { tabIndex = index },
                        text = {
                            Text(
                                text = text, color = if (selected) {
                                    MaterialTheme.colors.onBackground
                                } else {
                                    MaterialTheme.colors.onPrimary
                                }
                            )
                        }
                    )
                }
            }
            when (tabIndex) {
                profileIndex -> DistributorProfileInformationSection(
                    distributor = user!! as Distributor,
                    username = distributorAccountViewModel.usernameFieldText,
                    phone = distributorAccountViewModel.phoneFieldText,
                    rccm = distributorAccountViewModel.rccm,
                    taxId = distributorAccountViewModel.taxId,
                    websiteUrl = distributorAccountViewModel.websiteUrlFieldText,
                    isEditing = isEditing,
                    onEditingStart = { isEditing = true },
                    onEditingEnd = {
                        distributorAccountViewModel.updateDistributor()
                        isEditing = false
                    },
                    onEditingCancel = { isEditing = false },
                    loading = distributorAccountViewModel.accountApiCallResult.status == ApiStatus.LOADING,
                    onUsernameChange = { distributorAccountViewModel.usernameFieldText = it },
                    onUsernameValidated = { distributorAccountViewModel.username = it },
                    onPhoneChange = { distributorAccountViewModel.phoneFieldText = it },
                    onPhoneValidated = { distributorAccountViewModel.phone = it },
                    onRccmChange = { distributorAccountViewModel.rccmFieldText = it },
                    onRccmValidated = { distributorAccountViewModel.rccm = it },
                    onTaxIdChange = { distributorAccountViewModel.taxIdFieldText = it },
                    onTaxIdValidated = { distributorAccountViewModel.taxId = it },
                    onWebsiteUrlChange = { distributorAccountViewModel.websiteUrlFieldText = it },
                    onWebsiteUrlValidated = { distributorAccountViewModel.websiteUrl = it },
                    onDelete = {
                        distributorAccountViewModel.deleteDistributor {
                            coroutineScope.launch {
                                context.logout()
                                navController.navigate(Routes.Home.path) {
                                    popUpTo(Routes.Home.path) {
                                        inclusive = true
                                    }
                                }
                                snackbarHostState.showSnackbar(
                                    Strings.get(R.string.logout_successfull),
                                    Strings.get(R.string.ok),
                                    duration = SnackbarDuration.Long
                                )
                            }
                        }
                    }
                )
                locationsIndex -> DistributorLocationsScreen(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHostState = snackbarHostState
                )
                else -> Box(Modifier)
            }

            if (distributorAccountViewModel.accountApiCallResult.status == ApiStatus.SUCCESS) {
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = Strings.get(R.string.account_updated_successfully),
                            actionLabel = Strings.get(R.string.ok),
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            }

            if (distributorAccountViewModel.errorMessage() != null) {
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = distributorAccountViewModel.errorMessage()!!,
                            actionLabel = Strings.get(R.string.ok),
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            }
        }
    }
}

class DistributorAccountViewModel(
    private val accountService: AccountService = get(AccountService::class.java)
) : ViewModel() {
    private val distributor = (UserObserver.user.value!! as Distributor)
    private val token = TokenStore.get()!!
    var phoneFieldText by mutableStateOf(distributor.phone)
    var usernameFieldText by mutableStateOf(distributor.username)
    var taxIdFieldText by mutableStateOf(distributor.taxId)
    var rccmFieldText by mutableStateOf(distributor.rccm)
    var websiteUrlFieldText by mutableStateOf(distributor.websiteUrl ?: "")
    var phone by mutableStateOf(distributor.phone)
    var username by mutableStateOf(distributor.username)
    var taxId by mutableStateOf(distributor.taxId)
    var rccm by mutableStateOf(distributor.rccm)
    var websiteUrl by mutableStateOf(distributor.websiteUrl ?: "")

    var accountApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())

    fun updateDistributor() {
        if (phone.isBlank() || username.isBlank() || taxId.isBlank() || rccm.isBlank()) return
        accountApiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = accountService.updateDistributor(
                token.bearerToken,
                DistributorUpdateRequest(
                    username = username,
                    phone = phone,
                    taxId = taxId,
                    rccm = rccm,
                    websiteUrl = websiteUrl.ifBlank { null }
                )
            )
            accountApiCallResult = if (response.isSuccessful) {
                UserObserver.update((UserObserver.user.value as Distributor).copy(
                    username = username,
                    phone = phone,
                    taxId = taxId,
                    rccm = rccm,
                    websiteUrl = websiteUrl.ifBlank { null }
                ))
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun deleteDistributor(onDeleteSuccess: () -> Unit) {
        accountApiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = accountService.deleteAccount(token.bearerToken)
            accountApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit).also {
                    onDeleteSuccess()
                }
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (accountApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}