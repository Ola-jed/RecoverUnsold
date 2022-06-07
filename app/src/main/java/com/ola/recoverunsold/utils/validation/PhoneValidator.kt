package com.ola.recoverunsold.utils.validation

import android.content.res.Resources
import com.ola.recoverunsold.R

class PhoneValidator() : Validator {
    private val e164PhoneRegex = "^\\+?[1-9]\\d{1,14}\$".toRegex()

    override fun isValid(value: String): Boolean = e164PhoneRegex.matches(value)

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Resources.getSystem().getString(R.string.phone_invalid_message)
    }
}