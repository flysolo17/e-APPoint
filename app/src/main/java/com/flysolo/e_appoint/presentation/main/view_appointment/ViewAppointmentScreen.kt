package com.flysolo.e_appoint.presentation.main.view_appointment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.utils.BackButton

@Composable
fun ViewAppointmentDialog(
    modifier: Modifier = Modifier,
    appointments: Appointments,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit // Optional: If you want a confirmation action (e.g., Confirm, Edit)
) {
    // Dialog state for open/close
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        // Material Dialog
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Appointment Details", style = MaterialTheme.typography.titleLarge) },
            text = {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Doctor: JULIE ANN E. TEJEREO, DMD", style = MaterialTheme.typography.labelSmall)
                    Text("Date: ${appointments.schedules?.date}", style = MaterialTheme.typography.labelSmall)
                    Text("Time: ${appointments.schedules?.time}", style = MaterialTheme.typography.labelSmall)
                    Text("Client name: ${appointments.clientInfo?.name}", style = MaterialTheme.typography.labelSmall)
                    Text("Phone: ${appointments.clientInfo?.phone}", style = MaterialTheme.typography.labelSmall)
                    Text("Status: ${appointments.status}", style = MaterialTheme.typography.labelSmall)
                    Text("Notes: ${appointments.note ?: "No additional notes."}", style = MaterialTheme.typography.labelSmall)
                }
            },
            confirmButton = {

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        showDialog = false
                    }
                ) {
                    Text("Close")
                }
            },
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
