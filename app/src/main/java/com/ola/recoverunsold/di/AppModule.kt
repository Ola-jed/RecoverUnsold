package com.ola.recoverunsold.di

import com.ola.recoverunsold.api.core.ApiClient
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.api.services.AlertsService
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.api.services.ForgotPasswordService
import com.ola.recoverunsold.api.services.HomeService
import com.ola.recoverunsold.api.services.LocationService
import com.ola.recoverunsold.api.services.OfferService
import com.ola.recoverunsold.api.services.OpinionsService
import com.ola.recoverunsold.api.services.OrderService
import com.ola.recoverunsold.api.services.PaymentsService
import com.ola.recoverunsold.api.services.ReportsService
import com.ola.recoverunsold.api.services.ReviewsService
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.api.services.wrappers.DistributorServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.ProductServiceWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesAuthService() = ApiClient.buildService<AuthService>()

    @Provides
    @Singleton
    fun providesForgotPasswordService() = ApiClient.buildService<ForgotPasswordService>()

    @Provides
    @Singleton
    fun providesUserVerificationService() = ApiClient.buildService<UserVerificationService>()

    @Provides
    @Singleton
    fun providesAccountService() = ApiClient.buildService<AccountService>()

    @Provides
    @Singleton
    fun providesDistributorService() = ApiClient.buildService<DistributorService>()

    @Provides
    @Singleton
    fun providesLocationService() = ApiClient.buildService<LocationService>()

    @Provides
    @Singleton
    fun providesOfferService() = ApiClient.buildService<OfferService>()

    @Provides
    @Singleton
    fun providesOrderService() = ApiClient.buildService<OrderService>()

    @Provides
    @Singleton
    fun providesPaymentsService() = ApiClient.buildService<PaymentsService>()

    @Provides
    @Singleton
    fun providesOpinionsService() = ApiClient.buildService<OpinionsService>()

    @Provides
    @Singleton
    fun providesFcmTokenService() = ApiClient.buildService<FcmTokenService>()

    @Provides
    @Singleton
    fun providesReviewsService() = ApiClient.buildService<ReviewsService>()

    @Provides
    @Singleton
    fun providesAlertsService() = ApiClient.buildService<AlertsService>()

    @Provides
    @Singleton
    fun providesHomeService() = ApiClient.buildService<HomeService>()

    @Provides
    @Singleton
    fun providesReportsService() = ApiClient.buildService<ReportsService>()

    @Provides
    @Singleton
    fun providesLocationServiceWrapper() = LocationServiceWrapper(ApiClient.buildService())

    @Provides
    @Singleton
    fun providesOfferServiceWrapper() = OfferServiceWrapper(ApiClient.buildService())

    @Provides
    @Singleton
    fun providesOrderServiceWrapper() = OrderServiceWrapper(ApiClient.buildService())

    @Provides
    @Singleton
    fun providesProductServiceWrapper() = ProductServiceWrapper(ApiClient.buildService())

    @Provides
    @Singleton
    fun providesDistributorServiceWrapper() = DistributorServiceWrapper(ApiClient.buildService())
}