package com.ola.recoverunsold.ui.screens.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.api.requests.ProductUpdateRequest
import com.ola.recoverunsold.api.services.wrappers.ProductServiceWrapper
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.utils.misc.createFile
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

class ProductFormViewModel @AssistedInject constructor(
    private val productServiceWrapper: ProductServiceWrapper,
    @Assisted private val offerId: String,
    @Assisted private val product: Product? = null
) : ViewModel() {
    private val bearerToken = TokenStore.get()!!.bearerToken
    var formState by mutableStateOf(FormState())
    var name by mutableStateOf(product?.name ?: "")
    var description by mutableStateOf(product?.description ?: "")
    var images = mutableStateListOf<Uri>()
    var productApiCall: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)

    fun create(context: Context) {
        productApiCall = ApiCallResult.Loading
        val productCreateRequest = ProductCreateRequest(
            name = name,
            description = description,
            images = images.map { uri ->
                val imageFile = uri.createFile(context)
                MultipartBody.Part.createFormData(
                    "images",
                    imageFile.name,
                    imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }
        )
        viewModelScope.launch {
            val response = productServiceWrapper.createProduct(
                offerId,
                bearerToken,
                productCreateRequest
            )
            productApiCall = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun update() {
        productApiCall = ApiCallResult.Loading
        val productUpdateRequest = ProductUpdateRequest(
            name = name,
            description = description
        )
        viewModelScope.launch {
            val response = productServiceWrapper.updateProduct(
                product?.id!!,
                bearerToken,
                productUpdateRequest
            )
            productApiCall = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? {
        return when (productApiCall.statusCode) {
            400 -> Strings.get(R.string.invalid_data)
            in 400..600 -> Strings.get(R.string.unknown_error_occured)
            else -> null
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(offerId: String, product: Product?): ProductFormViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            productFormViewModelFactory: Factory,
            offerId: String,
            product: Product?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return productFormViewModelFactory.create(offerId, product) as T
            }
        }
    }
}