package com.ola.recoverunsold.utils.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.ola.recoverunsold.Token
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<Token> {
    override val defaultValue: Token = Token.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Token {
        try {
            return Token.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Token, output: OutputStream) = t.writeTo(output)
}

val Context.tokenDataStore: DataStore<Token> by dataStore(
    fileName = "tokens.pb",
    serializer = TokenSerializer
)