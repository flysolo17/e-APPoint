package com.flysolo.e_appoint.presentation.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.contentColorFor
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
import com.flysolo.e_appoint.utils.getColor
import java.util.Locale


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
                val color = appointments.status.getColor()
                Box(
                    modifier = modifier.wrapContentSize().background(
                        color = color,
                        shape = RoundedCornerShape(2.dp)
                    ).padding(2.dp)
                ) {
                    Text(appointments.status.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }, color = contentColorFor(color), style = MaterialTheme.typography.labelSmall
                    )
                }
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