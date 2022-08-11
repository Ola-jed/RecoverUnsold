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
import java.util.Date

@Composable
fun DatePicker(
    date: LocalDate? = null,
    minDate: Date? = null,
    maxDate: Date? = null,
    onDateUpdated: (LocalDate) -> Unit,
    show: Boolean,
    onCancel: () -> Unit
) {
    var internalDate by remember { mutableStateOf(date ?: LocalDate.now()) }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            internalDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateUpdated(internalDate)
        }, internalDate.year, internalDate.monthValue - 1, internalDate.dayOfMonth
    )
    datePickerDialog.setCancelable(true)
    datePickerDialog.setOnCancelListener {
        onCancel()
    }

    if (minDate != null) {
        datePickerDialog.datePicker.minDate = minDate.time
    }

    if (maxDate != null) {
        datePickerDialog.datePicker.maxDate = maxDate.time
    }

    if (show) {
        datePickerDialog.show()
    }
}