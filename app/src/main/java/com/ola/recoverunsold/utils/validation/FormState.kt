package com.ola.recoverunsold.utils.validation

/**
 * Data class to store our forms states
 */
data class FormState(var isValid: Boolean = false, var errorMessage: String? = null)