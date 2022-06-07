package com.ola.recoverunsold.utils.validation

import android.content.res.Resources
import com.ola.recoverunsold.R

class IsRequiredValidator : Validator {
    override fun isValid(value: String): Boolean = value.isNotBlank()

    override fun errorMessage(value: String): String? = if (isValid(value)) {
        null
    } else {
        Resources.getSystem().getString(R.string.required_field_invalid_message)
    }
}