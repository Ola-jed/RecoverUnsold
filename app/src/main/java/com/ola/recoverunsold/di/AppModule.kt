package com.ola.recoverunsold.di

import com.ola.recoverunsold.api.core.ApiClient
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.ForgotPasswordService
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.api.services.UserVerificationService
import org.koin.dsl.module

val appModule = module {
    factory<AuthService> { ApiClient.buildService() }
    factory<ForgotPasswordService> { ApiClient.buildService() }
    factory<UserVerificationService> { ApiClient.buildService() }
    factory<AccountService> { ApiClient.buildService() }
}