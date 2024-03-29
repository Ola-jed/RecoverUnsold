package com.ola.recoverunsold.ui.screens.distributor.offers

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DateTimePicker
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorOfferFormViewModel
import com.ola.recoverunsold.ui.theme.success
import com.ola.recoverunsold.utils.enums.FormType
import com.ola.recoverunsold.utils.extensions.addSeconds
import com.ola.recoverunsold.utils.extensions.formatDateTime
import com.ola.recoverunsold.utils.extensions.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.extensions.jsonDeserialize
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.extensions.toSecureDouble
import com.ola.recoverunsold.utils.extensions.toSecureInt
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IntegerValidator
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun DistributorOfferFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedOffer: String? = null,
    distributorOfferFormViewModel: DistributorOfferFormViewModel = distributorOfferFormViewModel(
        offer = serializedOffer.jsonDeserialize()
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val offer = serializedOffer.jsonDeserialize<Offer>()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        canGoBack = true,
                        navController = navController,
                        title = stringResource(
                            id = if (offer == null) {
                                R.string.create_new_offer
                            } else {
                                R.string.update_offer_label
                            }
                        )
                    )
                }
            ) { paddingValues ->
                DistributorOfferFormContent(
                    modifier = Modifier.padding(paddingValues),
                    formType = if (offer == null) FormType.Create else FormType.Update,
                    startDate = distributorOfferFormViewModel.startDate,
                    endDate = distributorOfferFormViewModel.endDate,
                    beneficiaries = distributorOfferFormViewModel.beneficiaries,
                    onlinePayment = distributorOfferFormViewModel.onlinePayment,
                    price = distributorOfferFormViewModel.price,
                    locations = distributorOfferFormViewModel.locations,
                    location = distributorOfferFormViewModel.location,
                    onStartDateChange = { distributorOfferFormViewModel.startDate = it },
                    onEndDateChange = { distributorOfferFormViewModel.endDate = it },
                    onBeneficiariesChange = { distributorOfferFormViewModel.beneficiaries = it },
                    onOnlinePaymentChange = { distributorOfferFormViewModel.onlinePayment = it },
                    onPriceChange = { distributorOfferFormViewModel.price = it },
                    onLocationChange = { distributorOfferFormViewModel.location = it },
                    onSubmit = {
                        if (!distributorOfferFormViewModel.formState.isValid) {
                            coroutineScope.launch {
                                snackbarHostState.show(
                                    message = distributorOfferFormViewModel.formState.errorMessage
                                        ?: Strings.get(R.string.invalid_data)
                                )
                            }
                        } else {
                            if (offer == null) {
                                distributorOfferFormViewModel.create()
                            } else {
                                distributorOfferFormViewModel.update()
                            }
                        }
                    },
                    loading = distributorOfferFormViewModel.offerResponse.status == ApiStatus.LOADING,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    errorMessage = distributorOfferFormViewModel.errorMessage(),
                    onValidationSuccess = {
                        distributorOfferFormViewModel.formState =
                            distributorOfferFormViewModel.formState
                                .copy(isValid = true, errorMessage = null)
                    },
                    onValidationError = {
                        distributorOfferFormViewModel.formState =
                            distributorOfferFormViewModel.formState
                                .copy(isValid = false, errorMessage = it)
                    },
                    isSuccessful = distributorOfferFormViewModel.offerResponse.status == ApiStatus.SUCCESS,
                    onSuccess = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(
                            Routes.OfferDetails.path.replace(
                                "{offerId}",
                                distributorOfferFormViewModel.offerResponse.data!!.id
                            )
                        )
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorOfferFormContent(
    modifier: Modifier,
    formType: FormType,
    startDate: Date?,
    endDate: Date?,
    beneficiaries: Int,
    onlinePayment: Boolean,
    price: Double,
    locations: List<Location>,
    location: Location?,
    onStartDateChange: (Date) -> Unit,
    onEndDateChange: (Date) -> Unit,
    onBeneficiariesChange: (Int) -> Unit,
    onOnlinePaymentChange: (Boolean) -> Unit,
    onPriceChange: (Double) -> Unit,
    onLocationChange: (Location) -> Unit,
    onSubmit: () -> Unit,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit,
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean,
    onSuccess: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showLocationsDropdown by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        val fieldsModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)

        CustomTextInput(
            modifier = fieldsModifier.clickable { showStartDatePicker = true },
            value = startDate?.formatDateTime() ?: "",
            readOnly = true,
            enabled = false,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.start_date_time_label)) },
            trailingIcon = { Icon(Icons.Default.Event, contentDescription = null) }
        )

        CustomTextInput(
            modifier = fieldsModifier.clickable { showEndDatePicker = true },
            value = endDate?.formatDateTime() ?: "",
            readOnly = true,
            enabled = false,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.end_date_time_label)) },
            trailingIcon = { Icon(Icons.Default.EventBusy, contentDescription = null) }
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = price.formatWithoutTrailingZeros(),
            onValueChange = { onPriceChange(it.toSecureDouble()) },
            label = { Text(text = stringResource(R.string.price_label)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IntegerValidator(),
            onValidationSuccess = onValidationSuccess,
            onValidationError = onValidationError
        )

        ExposedDropdownMenuBox(
            expanded = showLocationsDropdown,
            onExpandedChange = { showLocationsDropdown = !showLocationsDropdown }
        ) {
            CustomTextInput(
                modifier = fieldsModifier.menuAnchor(),
                value = location?.name ?: "",
                readOnly = true,
                onValueChange = {},
                label = { Text(text = stringResource(R.string.location)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showLocationsDropdown)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = showLocationsDropdown,
                onDismissRequest = { showLocationsDropdown = false }
            ) {
                locations.forEach {
                    DropdownMenuItem(
                        onClick = {
                            onLocationChange(it)
                            showLocationsDropdown = false
                        },
                        text = { Text(text = it.name) }
                    )
                }
            }
        }

        CustomTextInput(
            modifier = fieldsModifier,
            value = if (beneficiaries == 0) "" else beneficiaries.toString(),
            onValueChange = { onBeneficiariesChange(it.toSecureInt()) },
            label = { Text(text = stringResource(R.string.number_of_beneficiaries)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IntegerValidator(required = false),
            onValidationSuccess = onValidationSuccess,
            onValidationError = onValidationError,
            canBeEmpty = true
        )

        Row(
            modifier = fieldsModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.online_payment))
            Switch(
                checked = onlinePayment,
                onCheckedChange = onOnlinePaymentChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.success,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }

        Button(
            modifier = fieldsModifier.padding(top = 5.dp),
            onClick = onSubmit,
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
            } else {
                Text(stringResource(R.string.submit), modifier = Modifier.padding(5.dp))
            }
        }

        if (showStartDatePicker) {
            DateTimePicker(
                onDateUpdate = {
                    onStartDateChange(it)
                    showStartDatePicker = false
                },
                date = startDate,
                minDate = Date(),
                maxDate = endDate
            )
        }

        if (showEndDatePicker) {
            DateTimePicker(
                onDateUpdate = {
                    onEndDateChange(it)
                    showEndDatePicker = false
                },
                date = endDate,
                minDate = startDate,
                maxDate = startDate?.addSeconds(432000UL)
            )
        }

        if (errorMessage != null) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch {
                    snackbarHostState.show(message = errorMessage)
                }
            }
        }

        if (isSuccessful) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch {
                    snackbarHostState.show(
                        message = when (formType) {
                            FormType.Create -> Strings.get(R.string.offer_published_successfully)
                            FormType.Update -> Strings.get(R.string.offer_updated_successfully)
                        }
                    )
                    delay(500)
                    onSuccess()
                }
            }
        }
    }
}

@Composable
fun distributorOfferFormViewModel(offer: Offer?): DistributorOfferFormViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .distributorOfferFormViewModelFactory()
    return viewModel(factory = DistributorOfferFormViewModel.provideFactory(factory, offer))
}