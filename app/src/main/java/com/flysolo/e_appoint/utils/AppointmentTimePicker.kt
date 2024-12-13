package com.flysolo.e_appoint.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Calendar

import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentTimePicker(
    modifier: Modifier = Modifier,
    initialTime: String,
    onTimeSelected: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(initialTime) }

    // Restrict time selection to 8:00 AM - 12:00 PM and 1:00 PM - 5:00 PM
    val validTimeRange = listOf(
        Pair("08:00 AM", "12:00 PM"), // Morning range
        Pair("01:00 PM", "05:00 PM")  // Afternoon range
    )

    // Function to check if the selected time is within the allowed range
    fun isValidTime(time: String): Boolean {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val parsedTime = timeFormat.parse(time) ?: return false

        return validTimeRange.any { (start, end) ->
            val startTime = timeFormat.parse(start)
            val endTime = timeFormat.parse(end)
            parsedTime in startTime..endTime
        }
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = { },
            label = { Text("Select Time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Select time"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                onTimeSelected = { hour, minute ->
                    val time = String.format("%02d:%02d %s", if (hour > 12) hour - 12 else hour, minute, if (hour >= 12) "PM" else "AM")
                    if (isValidTime(time)) {
                        selectedTime = time
                        onTimeSelected(time)
                        showTimePicker = false
                    } else {
                        println("Invalid time selected: $time")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute
    )
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Time") },
        text = {
            androidx.compose.material3.TimePicker(
                state = timePickerState,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Get the selected time and pass it back to the parent
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
