package com.ola.recoverunsold

import android.app.Application
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.di.appModule
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.store.toApiToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.get
import java.util.Date

class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            modules(appModule)
        }
        GlobalScope.launch(Dispatchers.IO) {
            loadTokenAndUser()
        }
    }

    private suspend fun loadTokenAndUser() {
        val storedToken = TokenStore(applicationContext).token().firstOrNull()?.toApiToken()
        if (storedToken != null) {
            val token = TokenStore.getOr { storedToken }
            if (token.expirationDate.before(Date())) {
                TokenStore(applicationContext).removeToken()
                return
            }
            val accountService: AccountService = get(AccountService::class.java)
            val response = if (token.role == TokenRoles.CUSTOMER) {
                accountService.getCustomer(token.bearerToken)
            } else {
                accountService.getDistributor(token.bearerToken)
            }
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    UserObserver.update(user)
                }
            }
        }
    }
}