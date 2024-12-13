package com.flysolo.e_appoint.presentation.main.appointments

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.presentation.main.view_appointment.ViewAppointmentDialog
import com.flysolo.e_appoint.utils.CancelableIconButton


@Composable
fun AppointmentUserCard(
    modifier: Modifier = Modifier,
    appointments: Appointments,
    onClick : (String) -> Unit,
    onCancel : (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ViewAppointmentDialog(
            appointments = appointments,
            onDismiss = { showDialog = false },
            onConfirm = {
                println("Appointment confirmed!")
            }
        )
    }
    OutlinedCard(
        onClick = {
           showDialog = true
        },
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        ListItem(
            overlineContent = {
                Text("${appointments.status.name.lowercase()}")
            },
            headlineContent = {
                Text(
                    "${appointments.service}"
                )
            },
            supportingContent = {
                Text("${appointments.schedules?.date} - ${appointments.schedules?.time}")
            },


            trailingContent = {
                if (appointments.status == AppointmentStatus.PENDING) {
                    CancelableIconButton(
                        onCancel = { appointmentId ->
                            onCancel(appointmentId)
                            println("Cancel appointment with ID: $appointmentId")
                        },
                        appointmentId = appointments.id,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        )
    }
}