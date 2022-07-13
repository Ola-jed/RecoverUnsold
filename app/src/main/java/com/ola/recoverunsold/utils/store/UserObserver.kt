package com.ola.recoverunsold.utils.store

import com.ola.recoverunsold.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserObserver {
    private val userFlow = MutableStateFlow<User?>(null)
    val user = userFlow.asStateFlow()

    fun update(user: User?) {
        userFlow.value = user
    }

    fun remove() {
        update(null)
    }
}