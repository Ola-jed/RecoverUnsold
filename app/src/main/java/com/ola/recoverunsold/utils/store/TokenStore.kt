package com.ola.recoverunsold.utils.store

import android.content.Context
import androidx.datastore.core.DataStore
import com.google.protobuf.Timestamp
import com.ola.recoverunsold.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class TokenStore(context: Context) {
    private val dataStore: DataStore<Token> = context.tokenDataStore

    fun token(): Flow<Token> = dataStore.data.map { it }

    suspend fun storeToken(apiToken: com.ola.recoverunsold.api.responses.Token) {

        dataStore.updateData { token ->
            token.toBuilder()
                .setRole(apiToken.role)
                .setToken(apiToken.token)
                .setExpirationDate(
                    Timestamp.newBuilder()
                        .setSeconds(apiToken.expirationDate.time)
                ).build()
        }
    }

    suspend fun removeToken() {
        dataStore.updateData { it.toBuilder().clear().build() }
    }

    companion object {
        lateinit var token: com.ola.recoverunsold.api.responses.Token

        fun init(producer: () -> com.ola.recoverunsold.api.responses.Token) {
            token = producer()
        }

        fun get(): com.ola.recoverunsold.api.responses.Token? = if (::token.isInitialized) {
            token
        } else {
            null
        }

        fun getOr(producer: () -> com.ola.recoverunsold.api.responses.Token)
                : com.ola.recoverunsold.api.responses.Token {
            return if (::token.isInitialized) {
                token
            } else {
                token = producer()
                token
            }
        }
    }
}

fun Token.toApiToken(): com.ola.recoverunsold.api.responses.Token {
    return com.ola.recoverunsold.api.responses.Token(
        role = role,
        token = token,
        expirationDate = Date(expirationDate.seconds * 1000)
    )
}