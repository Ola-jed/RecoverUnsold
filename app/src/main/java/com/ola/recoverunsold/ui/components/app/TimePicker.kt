package com.ola.recoverunsold.ui.components.app

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimePicker(
    time: LocalTime? = null,
    onTimeUpdated: (LocalTime?) -> Unit,
    show: Boolean,
    onCancel: () -> Unit
) {
    var internalTime by remember { mutableStateOf(time ?: LocalTime.now()) }
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            internalTime = LocalTime.of(hourOfDay, minute)
            onTimeUpdated(internalTime)
        }, internalTime.hour, internalTime.minute, true
    )
    timePickerDialog.setCancelable(true)
    timePickerDialog.setOnCancelListener {
        onTimeUpdated(null)
        onCancel()
    }

    if (show) {
        timePickerDialog.show()
    }
}