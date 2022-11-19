package com.ola.recoverunsold.ui.screens.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.LocationCreateOrUpdateRequest
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.utils.misc.createFile
import com.ola.recoverunsold.utils.misc.nullIfBlank
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.validation.FormState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class DistributorLocationFormViewModel @AssistedInject constructor(
    private val locationServiceWrapper: LocationServiceWrapper,
    @Assisted private val location: Location?
) : ViewModel() {
    var formState by mutableStateOf(FormState())
    var apiCallResult: ApiCallResult<Location> by mutableStateOf(ApiCallResult.Inactive)
    var name by mutableStateOf(location?.name ?: "")
    var indication by mutableStateOf(location?.indication ?: "")
    var imageUri by mutableStateOf<Uri?>(null)
    var latLong by mutableStateOf(location?.coordinates ?: LatLong.zero)
    val token = TokenStore.get()!!.bearerToken

    fun create(context: Context) {
        apiCallResult = ApiCallResult.Loading
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
            apiCallResult = locationServiceWrapper
                .createLocation(locationRequest)
                .toApiCallResult()
        }
    }

    fun update(context: Context) {
        apiCallResult = ApiCallResult.Loading
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
            val response = locationServiceWrapper.updateLocation(location?.id!!, locationRequest)
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

    @AssistedFactory
    interface Factory {
        fun create(location: Location?): DistributorLocationFormViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            distributorLocationFormViewModelFactory: Factory,
            location: Location?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return distributorLocationFormViewModelFactory.create(location) as T
            }
        }
    }
}