package com.ola.recoverunsold.utils.validation

interface Validator {
    fun isValid(value: String): Boolean
    fun errorMessage(value: String): String?
}