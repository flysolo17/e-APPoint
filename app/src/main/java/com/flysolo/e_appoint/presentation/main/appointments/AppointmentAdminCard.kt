package com.flysolo.e_appoint.presentation.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.presentation.main.view_appointment.ViewAppointmentDialog
import com.flysolo.e_appoint.utils.CancelableIconButton
import com.flysolo.e_appoint.utils.getColor
import java.util.Locale


@Composable
fun AppointmentAdminCard(
    modifier: Modifier = Modifier,
    appointments: Appointments,
    onClick : (String) -> Unit,
    onDecline : (String) -> Unit,
    onConfirmed : (String) -> Unit,
    onComplete : (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ViewAppointmentDialog(
            appointments = appointments,
            onDismiss = { showDialog = false },
            onConfirm = {

                println("Appointment Confirmed!")
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CancelableIconButton(
                            onCancel = { appointmentId ->
                                onDecline(appointmentId)
                            },
                            appointmentId = appointments.id,
                            confirmationMessage = "Are you sure you want to decline this appointment?"
                        )
                        FilledIconButton(
                            shape = CircleShape,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                            appointments.id?.let {
                                onConfirmed(it)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm"
                            )
                        }
                    }
                } else if (appointments.status == AppointmentStatus.CONFIRMED) {
                    FilledIconButton(
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF006400),
                            contentColor = Color.White
                        ),
                        onClick = {
                            appointments.id?.let {
                                onComplete(it)
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Complete"
                        )
                    }
                }
            }
        )
    }
}