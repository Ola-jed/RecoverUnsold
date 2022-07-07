package com.ola.recoverunsold.utils.validation

import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings

class IntegerValidator : Validator {
    private val integerRegex = "[0-9]+".toRegex()

    override fun isValid(value: String): Boolean = integerRegex.matches(value)

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Strings.get(R.string.invalid_integer)
    }
}