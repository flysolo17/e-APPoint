package com.flysolo.e_appoint.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDatePicker(
    modifier: Modifier = Modifier,
    value: String,
    onDateSelected: (String) -> Unit,
) {
    // Disable Saturday and Sunday (or customize this logic as needed)
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text("Select Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        title = { DatePickerDefaults.DatePickerTitle(displayMode = datePickerState.displayMode) },
                        headline = {
                            DatePickerDefaults.DatePickerHeadline(
                                selectedDateMillis = datePickerState.selectedDateMillis,
                                displayMode = datePickerState.displayMode,
                                dateFormatter = DatePickerDefaults.dateFormatter(),
                            )
                        }
                    )
                    // Listen for date selection and pass the result to the parent
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val selectedDate = convertMillisToDate(millis)
                            onDateSelected(selectedDate)
                            showDatePicker = false
                        }
                    }
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Example format: "Dec 13, 2024"
    return dateFormat.format(Date(millis))
}