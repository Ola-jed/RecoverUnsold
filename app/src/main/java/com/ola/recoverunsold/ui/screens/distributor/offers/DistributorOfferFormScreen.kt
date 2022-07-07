package com.ola.recoverunsold.ui.screens.distributor.offers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorOfferFormViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorOfferFormViewModelFactory
import com.ola.recoverunsold.utils.misc.jsonDeserialize
import kotlinx.coroutines.CoroutineScope
import java.util.Date

// TODO : finish

@Composable
fun DistributorOfferFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedOffer: String? = null,
    distributorOfferFormViewModel: DistributorOfferFormViewModel = viewModel(
        factory = DistributorOfferFormViewModelFactory(serializedOffer.jsonDeserialize<Offer>())
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val offer = serializedOffer.jsonDeserialize<Offer>()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController
            )
        }
    ) { paddingValues ->
        DistributorOfferFormContent(
            modifier = Modifier
                .padding(paddingValues),
            formType = if (offer == null) FormType.Create else FormType.Update,
            startDate = distributorOfferFormViewModel.startDate,
            endDate = distributorOfferFormViewModel.endDate,
            beneficiaries = distributorOfferFormViewModel.beneficiaries,
            price = distributorOfferFormViewModel.price,
            locations = distributorOfferFormViewModel.locations,
            location = distributorOfferFormViewModel.location,
            products = distributorOfferFormViewModel.products,
            onStartDateChange = { distributorOfferFormViewModel.startDate = it },
            onEndDateChange = { distributorOfferFormViewModel.endDate = it },
            onBeneficiariesChange = { distributorOfferFormViewModel.beneficiaries = it },
            onPriceChange = { distributorOfferFormViewModel.price = it },
            onLocationChange = { distributorOfferFormViewModel.location = it },
            onSubmit = {/* TODO */ },
            loading = false,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            errorMessage = distributorOfferFormViewModel.errorMessage(),
            isSuccessful = false
        )
    }
}

@Composable
fun DistributorOfferFormContent(
    modifier: Modifier,
    formType: FormType,
    startDate: Date,
    endDate: Date,
    beneficiaries: Int,
    price: Double,
    locations: List<Location>,
    location: Location?,
    products: List<ProductCreateRequest>? = null,
    onStartDateChange: (Date) -> Unit,
    onEndDateChange: (Date) -> Unit,
    onBeneficiariesChange: (Int) -> Unit,
    onPriceChange: (Double) -> Unit,
    onLocationChange: (Location) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when(formType) {
            FormType.Create -> Text(stringResource(id = R.string.create_new_offer))

        }
    }
}

enum class FormType {
    Create,
    Update
}