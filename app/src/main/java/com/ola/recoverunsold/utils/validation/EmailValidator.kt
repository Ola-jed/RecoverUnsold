package com.ola.recoverunsold.utils.validation

import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings

class EmailValidator : Validator {
    private val emailRegex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+".toRegex()

    override fun isValid(value: String): Boolean = emailRegex.matches(value)

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Strings.get(R.string.email_invalid_message)
    }
}