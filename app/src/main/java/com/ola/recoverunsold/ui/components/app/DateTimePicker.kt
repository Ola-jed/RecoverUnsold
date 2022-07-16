package com.ola.recoverunsold.ui.components.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

/**
 * Component to show the date picker and immediately after the time picker
 */
@Composable
fun DateTimePicker(
    date: Date? = null,
    onDateUpdate: (Date) -> Unit
) {
    val dateTime = LocalDateTime.ofInstant((date ?: Date()).toInstant(), ZoneId.systemDefault())
    var localDate by remember { mutableStateOf(dateTime.toLocalDate()) }
    var localTime by remember { mutableStateOf(dateTime.toLocalTime()) }
    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePicker(
            date = localDate,
            onDateUpdated = {
                localDate = it
                showDatePicker = false
                showTimePicker = true
            },
            show = showDatePicker,
            onCancel = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        TimePicker(
            time = localTime,
            onTimeUpdated = {
                localTime = it
                onDateUpdate(buildDate(localDate, localTime))
                showTimePicker = false
            },
            show = showTimePicker,
            onCancel = { showTimePicker = false }
        )
    }
}

private fun buildDate(localDate: LocalDate, localTime: LocalTime): Date {
    return Date.from(
        LocalDateTime.of(localDate, localTime)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    )
}