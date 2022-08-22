package com.ola.recoverunsold.ui.screens.distributor.offers

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.ImagePicker
import com.ola.recoverunsold.ui.components.app.LocalImagesList
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.ProductFormViewModel
import com.ola.recoverunsold.utils.misc.FormType
import com.ola.recoverunsold.utils.misc.jsonDeserialize
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DistributorProductFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offerId: String,
    serializedProduct: String? = null,
    productFormViewModel: ProductFormViewModel = productFormViewModel(
        offerId = offerId,
        product = serializedProduct.jsonDeserialize()
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController,
                title = stringResource(
                    id = if (serializedProduct == null) {
                        R.string.new_product
                    } else {
                        R.string.update_product
                    }
                )
            )
        },
        drawerContent = DrawerContent(navController),
    ) { paddingValues ->
        DistributorProductFormScreenContent(
            modifier = Modifier.padding(paddingValues),
            formType = if (serializedProduct.isNullOrBlank()) FormType.Create else FormType.Update,
            name = productFormViewModel.name,
            description = productFormViewModel.description,
            imageUris = productFormViewModel.images,
            currentImageUri = productFormViewModel.images.lastOrNull(),
            onNameChanged = { productFormViewModel.name = it },
            onDescriptionChanged = { productFormViewModel.description = it },
            onImageAdded = { productFormViewModel.images.add(it) },
            onImageDeleted = { productFormViewModel.images.removeIf { uri -> uri == it } },
            onValidationSuccess = {
                productFormViewModel.formState = productFormViewModel.formState
                    .copy(
                        isValid = true,
                        errorMessage = null
                    )
            },
            onValidationError = {
                productFormViewModel.formState = productFormViewModel.formState
                    .copy(
                        isValid = false,
                        errorMessage = it
                    )
            },
            loading = productFormViewModel.productApiCall.status == ApiStatus.LOADING,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            isSuccessful = productFormViewModel.productApiCall.status == ApiStatus.SUCCESS,
            onSubmit = {
                if (!productFormViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = productFormViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    if (serializedProduct.isNullOrBlank()) {
                        productFormViewModel.create(context)
                    } else {
                        productFormViewModel.update()
                    }
                }
            },
            onSuccess = {
                navController.popBackStack()
                navController.popBackStack()
                navController.navigate(
                    Routes.OfferDetails.path.replace("{offerId}", offerId)
                )
            }
        )
    }
}

@Composable
fun DistributorProductFormScreenContent(
    modifier: Modifier,
    formType: FormType,
    name: String,
    description: String,
    currentImageUri: Uri? = null,
    imageUris: List<Uri> = emptyList(),
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onImageAdded: (Uri) -> Unit,
    onImageDeleted: (Uri) -> Unit,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit,
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean,
    onSuccess: () -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    val imageListHeight = (LocalConfiguration.current.screenHeightDp * 0.25).dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = when (formType) {
                FormType.Create -> stringResource(id = R.string.add_product_to_offer)
                FormType.Update -> stringResource(id = R.string.update_product)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp, bottom = 25.dp),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = name,
            leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.name)) },
            label = { Text(text = stringResource(R.string.name)) },
            onValueChange = onNameChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidationSuccess = onValidationSuccess,
            onValidationError = onValidationError
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = description,
            leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.description)) },
            label = { Text(text = stringResource(R.string.description)) },
            onValueChange = onDescriptionChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidationSuccess = onValidationSuccess,
            onValidationError = onValidationError
        )

        if (formType == FormType.Create) {
            ImagePicker(
                modifier = fieldsModifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.25).dp)
                    .padding(bottom = 25.dp),
                imageUri = currentImageUri,
                onImagePicked = onImageAdded
            )

            if (imageUris.isNotEmpty()) {
                LocalImagesList(
                    modifier = fieldsModifier
                        .height(imageListHeight)
                        .padding(bottom = 25.dp),
                    imageUris = imageUris,
                    onDelete = onImageDeleted
                )
            }
        }

        Button(modifier = fieldsModifier, onClick = onSubmit, enabled = !loading) {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            } else {
                Text(stringResource(R.string.submit), modifier = Modifier.padding(5.dp))
            }
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
                            FormType.Create -> Strings.get(R.string.product_published_successfully)
                            FormType.Update -> Strings.get(R.string.product_updated_successfully)
                        }
                    )
                    delay(900)
                    onSuccess()
                }
            }
        }
    }
}

@Composable
fun productFormViewModel(offerId: String, product: Product?): ProductFormViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .productFormViewModelFactory()
    return viewModel(factory = ProductFormViewModel.provideFactory(factory, offerId, product))
}
