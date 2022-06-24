package com.ola.recoverunsold.utils.store

import com.ola.recoverunsold.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserObserver {
    private val userFlow: MutableStateFlow<User?> = MutableStateFlow(null)
    var user = userFlow.asStateFlow()

    fun update(user: User?) {
        userFlow.value = user
    }

    fun update(provider: () -> User?) {
        userFlow.value = provider()
    }

    fun remove() {
        update(null)
    }
}