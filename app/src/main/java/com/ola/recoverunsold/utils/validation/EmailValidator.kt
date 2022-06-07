package com.ola.recoverunsold.utils.validation

import android.content.res.Resources
import com.ola.recoverunsold.R

class EmailValidator() : Validator {
    private val emailRegex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+".toRegex()

    override fun isValid(value: String): Boolean = emailRegex.matches(value)

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Resources.getSystem().getString(R.string.email_invalid_message)
    }
}