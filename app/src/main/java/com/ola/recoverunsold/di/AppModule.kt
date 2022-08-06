package com.ola.recoverunsold.di

import com.ola.recoverunsold.api.core.ApiClient
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.api.services.AlertsService
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.api.services.ForgotPasswordService
import com.ola.recoverunsold.api.services.LocationService
import com.ola.recoverunsold.api.services.OfferService
import com.ola.recoverunsold.api.services.OpinionsService
import com.ola.recoverunsold.api.services.OrderService
import com.ola.recoverunsold.api.services.ReviewsService
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.api.services.wrappers.DistributorServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.ProductServiceWrapper
import org.koin.dsl.module

val appModule = module {
    factory<AuthService> { ApiClient.buildService() }
    factory<ForgotPasswordService> { ApiClient.buildService() }
    factory<UserVerificationService> { ApiClient.buildService() }
    factory<AccountService> { ApiClient.buildService() }
    factory<DistributorService> { ApiClient.buildService() }
    factory<LocationService> { ApiClient.buildService() }
    factory<OfferService> { ApiClient.buildService() }
    factory<OrderService> { ApiClient.buildService() }
    factory<OpinionsService> { ApiClient.buildService() }
    factory<FcmTokenService> { ApiClient.buildService() }
    factory<ReviewsService> { ApiClient.buildService() }
    factory<AlertsService> { ApiClient.buildService() }
    factory { LocationServiceWrapper(ApiClient.buildService()) }
    factory { OfferServiceWrapper(ApiClient.buildService()) }
    factory { OrderServiceWrapper(ApiClient.buildService()) }
    factory { ProductServiceWrapper(ApiClient.buildService()) }
    factory { DistributorServiceWrapper(ApiClient.buildService()) }
}