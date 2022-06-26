package com.ola.recoverunsold.utils.misc

import android.content.Context
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver

suspend fun Context.logout() {
    TokenStore(this).removeToken()
    UserObserver.remove()
}