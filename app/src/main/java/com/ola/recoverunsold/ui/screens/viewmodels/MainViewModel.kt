package com.ola.recoverunsold.ui.screens.viewmodels

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.AppPreferences
import com.ola.recoverunsold.utils.store.LocaleObserver
import com.ola.recoverunsold.utils.store.ThemeObserver
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.store.toApiToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val accountService: AccountService) : ViewModel() {
    private val _hasFetchedData = MutableStateFlow(false)
    val hasFinishedLoading = _hasFetchedData.asStateFlow()

    fun initializeApp(context: Context) {
        setTheme(context)
        setLocale(context)
        viewModelScope.launch {
            val storedToken = TokenStore(context)
                .token()
                .firstOrNull()
                ?.toApiToken()

            try {
                if (storedToken != null) {
                    val token = TokenStore.getOr { storedToken }
                    if (token.expirationDate.before(Date())) {
                        defineShortcutsAnonymously(context)
                        TokenStore(context).removeToken()
                    } else {
                        val response = if (token.role == TokenRoles.CUSTOMER) {
                            defineShortcutsAsCustomer(context)
                            accountService.getCustomer()
                        } else {
                            defineShortcutsAsDistributor(context)
                            accountService.getDistributor()
                        }
                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                UserObserver.update(user)
                            }
                        }
                    }
                } else {
                    defineShortcutsAnonymously(context)
                }
            } catch (e: Exception) {
                // Nothing
            }
            _hasFetchedData.value = true
        }
    }

    private fun setTheme(context: Context) {
        ThemeObserver.update(AppPreferences.getTheme(context))
    }

    private fun setLocale(context: Context) {
        val locale = AppPreferences.getLocale(context)
//        val phoneLocale = AndroidLocale(locale.code)
//        val resources: Resources = context.resources
//        val config: Configuration = resources.configuration
//        config.setLocale(phoneLocale)
//        context.createConfigurationContext(config)
//        context.resources.updateConfiguration(config, context.resources.displayMetrics)
//        AndroidLocale.setDefault(phoneLocale)
        LocaleObserver.update(locale)
    }

    private fun defineShortcutsAnonymously(context: Context) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = listOf(
            ShortcutInfo.Builder(context, Strings.get(R.string.offers))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/offers"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(0)
                .setIcon(Icon.createWithResource(context, R.drawable.shopping_bag))
                .setLongLabel(Strings.get(R.string.offers))
                .setShortLabel(Strings.get(R.string.offers))
                .build(),
            ShortcutInfo.Builder(context, Strings.get(R.string.distributors))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/distributors"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(1)
                .setIcon(Icon.createWithResource(context, R.drawable.shopping_cart))
                .setLongLabel(Strings.get(R.string.distributors))
                .setShortLabel(Strings.get(R.string.distributors))
                .build()
        )
    }

    private fun defineShortcutsAsCustomer(context: Context) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = listOf(
            ShortcutInfo.Builder(context, Strings.get(R.string.offers))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/offers"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(0)
                .setIcon(Icon.createWithResource(context, R.drawable.shopping_bag))
                .setLongLabel(Strings.get(R.string.offers))
                .setShortLabel(Strings.get(R.string.offers))
                .build(),
            ShortcutInfo.Builder(context, Strings.get(R.string.distributors))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/distributors"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(1)
                .setIcon(Icon.createWithResource(context, R.drawable.shopping_cart))
                .setLongLabel(Strings.get(R.string.distributors))
                .setShortLabel(Strings.get(R.string.distributors))
                .build(),
            ShortcutInfo.Builder(context, Strings.get(R.string.settings))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/account/customer"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(2)
                .setIcon(Icon.createWithResource(context, R.drawable.setting))
                .setLongLabel(Strings.get(R.string.settings))
                .setShortLabel(Strings.get(R.string.settings))
                .build()
        )
    }

    private fun defineShortcutsAsDistributor(context: Context) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = listOf(
            ShortcutInfo.Builder(
                context,
                Strings.get(R.string.offers_published)
            )
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/distributor/offers"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(0)
                .setIcon(Icon.createWithResource(context, R.drawable.shopping_bag))
                .setLongLabel(Strings.get(R.string.offers_published))
                .setShortLabel(Strings.get(R.string.offers_published))
                .build(),
            ShortcutInfo.Builder(context, Strings.get(R.string.orders_received))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/orders/received"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(1)
                .setIcon(Icon.createWithResource(context, R.drawable.history))
                .setLongLabel(Strings.get(R.string.orders_received))
                .setShortLabel(Strings.get(R.string.orders_received))
                .build(),
            ShortcutInfo.Builder(context, Strings.get(R.string.settings))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://com.ola.recoverunsold/account/distributor"),
                        context,
                        MainActivity::class.java
                    )
                )
                .setRank(2)
                .setIcon(Icon.createWithResource(context, R.drawable.setting))
                .setLongLabel(Strings.get(R.string.settings))
                .setShortLabel(Strings.get(R.string.settings))
                .build()
        )
    }
}