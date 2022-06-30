package com.ola.recoverunsold.ui.components.app

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun DatePicker(
    date: LocalDate? = null,
    onDateUpdated: (LocalDate) -> Unit,
    show: Boolean,
    onCancel: () -> Unit
) {
    var internalDate by remember { mutableStateOf(date ?: LocalDate.now()) }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            internalDate = LocalDate.of(year, month, dayOfMonth)
            onDateUpdated(internalDate)
        }, internalDate.year, internalDate.monthValue, internalDate.dayOfMonth
    )
    datePickerDialog.setCancelable(true)
    datePickerDialog.setOnCancelListener {
        onCancel()
    }

    if (show) {
        datePickerDialog.show()
    }
}