package com.ola.recoverunsold.utils.validation

import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings

class IsRequiredValidator : Validator {
    override fun isValid(value: String): Boolean = value.trim().isNotBlank()

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Strings.get(R.string.required_field_invalid_message)
    }
}