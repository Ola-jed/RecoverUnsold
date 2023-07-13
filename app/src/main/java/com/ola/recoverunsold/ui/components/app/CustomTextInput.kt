package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.Validator

@Composable
fun CustomTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    validator: Validator? = null,
    onValidationError: ((String) -> Unit)? = null,
    onValidationSuccess: (() -> Unit)? = null,
    canBeEmpty: Boolean = false
) {
    val valueIsInvalid = (!canBeEmpty && value.isNotBlank())
            && validator?.isValid(value.trimEnd()) == false
    Column(
        modifier = Modifier
            .padding(bottom = if (valueIsInvalid) 0.dp else 10.dp)
    ) {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = {
                onValueChange(it)
                if (!valueIsInvalid && (it.isNotBlank() xor canBeEmpty)) {
                    onValidationSuccess?.invoke()
                }
            },
            modifier = modifier,
            singleLine = singleLine,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = valueIsInvalid,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )

        if (valueIsInvalid) {
            if (onValidationError != null) {
                onValidationError(validator?.errorMessage(value) ?: "")
            }
            Text(
                text = validator?.errorMessage(value) ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        } else if (value.isBlank() && !canBeEmpty) {
            onValidationError?.invoke(Strings.get(R.string.invalid_data))
        }
    }
}
