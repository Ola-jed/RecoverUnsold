package com.ola.recoverunsold.ui.screens.distributor.account

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.requests.LocationCreateOrUpdateRequest
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.ImagePicker
import com.ola.recoverunsold.ui.components.location.LocationMap
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.createFile
import com.ola.recoverunsold.utils.misc.jsonDeserialize
import com.ola.recoverunsold.utils.misc.nullIfBlank
import com.ola.recoverunsold.utils.misc.toCoordinates
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.java.KoinJavaComponent.get
import kotlin.ranges.contains

@Composable
fun DistributorLocationFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedLocation: String? = null,
    distributorLocationFormViewModel: DistributorLocationFormViewModel = viewModel(
        factory = DistributorLocationFormViewModelFactory(serializedLocation.jsonDeserialize<Location>())
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val location = serializedLocation.jsonDeserialize<Location>()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController
            )
        },
        drawerGesturesEnabled = false
    ) { paddingValues ->
        DistributorLocationFormScreenContent(
            modifier = Modifier
                .padding(paddingValues),
            name = distributorLocationFormViewModel.name,
            indication = distributorLocationFormViewModel.indication,
            location = location,
            imageUri = distributorLocationFormViewModel.imageUri,
            loading = distributorLocationFormViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = distributorLocationFormViewModel.errorMessage(),
            isSuccessful = distributorLocationFormViewModel.apiCallResult.status == ApiStatus.SUCCESS,
            onImagePicked = { distributorLocationFormViewModel.imageUri = it },
            onNameChange = { distributorLocationFormViewModel.nameFieldText = it },
            onNameValidated = { distributorLocationFormViewModel.name = it },
            onIndicationChange = { distributorLocationFormViewModel.indication = it },
            onLatLngUpdate = { distributorLocationFormViewModel.latLong = it.toCoordinates() },
            onSubmit = {
                if (location == null) {
                    distributorLocationFormViewModel.create(context)
                } else {
                    distributorLocationFormViewModel.update(context)
                }
            },
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            navController = navController
        )
    }
}

@Composable
fun DistributorLocationFormScreenContent(
    modifier: Modifier = Modifier,
    name: String,
    indication: String,
    location: Location? = null,
    imageUri: Uri? = null,
    loading: Boolean,
    isSuccessful: Boolean,
    errorMessage: String?,
    onImagePicked: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onNameValidated: (String) -> Unit,
    onIndicationChange: (String) -> Unit,
    onLatLngUpdate: (LatLng) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    navController: NavController
) {
    var currentIndex by remember { mutableStateOf(0) }
    val maxIndex = 2

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(top = 25.dp, bottom = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        val fieldsModifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)

        when (currentIndex) {
            0 -> Column(modifier = Modifier.padding(top = 25.dp)) {
                CustomTextInput(
                    modifier = fieldsModifier,
                    value = name,
                    leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
                    placeholder = { Text(text = stringResource(R.string.name)) },
                    label = { Text(text = stringResource(R.string.name)) },
                    onValueChange = onNameChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Ascii
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    validator = IsRequiredValidator(),
                    onValidatedValue = onNameValidated
                )

                CustomTextInput(
                    modifier = fieldsModifier,
                    value = indication,
                    leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
                    placeholder = { Text(text = stringResource(R.string.indication)) },
                    label = { Text(text = stringResource(R.string.indication)) },
                    onValueChange = onIndicationChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = false,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )
            }
            1 -> ImagePicker(
                modifier = fieldsModifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.25).dp),
                imageUri = imageUri,
                onImagePicked = onImagePicked
            )
            2 -> Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    stringResource(id = R.string.choose_location_on_map),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 15.dp, start = 10.dp, end = 10.dp),
                    style = MaterialTheme.typography.h6
                )

                LocationMap(
                    modifier = Modifier
                        .size(
                            height = (LocalConfiguration.current.screenHeightDp * 0.5).dp,
                            width = (LocalConfiguration.current.screenHeightDp * 0.7).dp
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    latLng = if (location != null) {
                        LatLng(location.coordinates.latitude, location.coordinates.longitude)
                    } else {
                        null
                    },
                    onLatLngUpdate = onLatLngUpdate,
                    onLocationFetchFailed = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = Strings.get(R.string.location_fetch_failed),
                                actionLabel = Strings.get(R.string.ok),
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (currentIndex > 0) {
                Button(onClick = {
                    currentIndex--
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Text(text = stringResource(id = R.string.previous))
                }
            }

            if (currentIndex < maxIndex) {
                Button(onClick = {
                    currentIndex++
                }) {
                    Text(text = stringResource(id = R.string.next))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }

            if (currentIndex == maxIndex) {
                if (loading) {
                    Button(onClick = {}) {
                        CircularProgressIndicator(color = MaterialTheme.colors.background)
                    }
                } else {
                    Button(onClick = onSubmit) {
                        Text(text = stringResource(R.string.submit))
                        Icon(Icons.Default.Send, contentDescription = null)
                    }
                }
            }
        }

        if (errorMessage != null) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }

        if (isSuccessful) {
            LaunchedEffect(snackbarHostState) {
                navController.navigate(Routes.DistributorAccount.path)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = Strings.get(R.string.location_published_successfully),
                        actionLabel = Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }
}

class DistributorLocationFormViewModelFactory(private val location: Location?) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DistributorLocationFormViewModel(location = location) as T
    }
}

class DistributorLocationFormViewModel(
    private val locationServiceWrapper: LocationServiceWrapper = get(LocationServiceWrapper::class.java),
    private val location: Location?
) : ViewModel() {
    var apiCallResult: ApiCallResult<Location> by mutableStateOf(ApiCallResult.Inactive())
    var nameFieldText by mutableStateOf(location?.name ?: "")
    var name by mutableStateOf(location?.name ?: "")
    var indication by mutableStateOf(location?.indication ?: "")
    var imageUri by mutableStateOf<Uri?>(null)
    var latLong by mutableStateOf(location?.coordinates ?: LatLong.zero())

    fun create(context: Context) {
        if (name.isBlank()) return

        apiCallResult = ApiCallResult.Loading()
        val token = TokenStore.get()!!
        val locationRequest = LocationCreateOrUpdateRequest(
            name = name,
            indication = indication.nullIfBlank(),
            latitude = latLong.latitude,
            longitude = latLong.longitude,
            image = if (imageUri == null) {
                null
            } else {
                val imageFile = imageUri!!.createFile(context)
                MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }
        )
        viewModelScope.launch {
            val response = locationServiceWrapper.createLocation(
                token.bearerToken,
                locationRequest
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun update(context: Context) {
        if (name.isBlank()) return

        apiCallResult = ApiCallResult.Loading()
        val token = TokenStore.get()!!
        val locationRequest = LocationCreateOrUpdateRequest(
            name = name,
            indication = indication.nullIfBlank(),
            latitude = latLong.latitude,
            longitude = latLong.longitude,
            image = if (imageUri == null) {
                null
            } else {
                val imageFile = imageUri!!.createFile(context)
                MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }
        )
        viewModelScope.launch {
            val response = locationServiceWrapper.updateLocation(
                token.bearerToken,
                location?.id!!,
                locationRequest
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Location.Dummy)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}