package com.flysolo.e_appoint.presentation.main.appointments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.sourceInformationMarkerStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.models.users.UserType
import com.flysolo.e_appoint.utils.shortToast


@Composable
fun AppointmentsScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    events: (AppointmentEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.users) {
        if (state.users != null) {
            if (state.users.type == UserType.ADMIN) {
                events.invoke(AppointmentEvents.OnGetAllAppointments)
            } else {
                events.invoke(AppointmentEvents.OnGetMyAppointments(state.users.id  ?: "" ))
            }
        }
    }
    LaunchedEffect(state.errors) {
        state.errors?.let {
            context.shortToast(state.errors)
        }
    }
    LaunchedEffect(state.messages) {
        state.messages?.let {
            context.shortToast(state.messages)
        }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            item{
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }

        items(state.appointments) {
            if (state.users?.type == UserType.ADMIN) {
                AppointmentAdminCard(appointments = it, onClick = {}, onCancel = {})
            } else {
                AppointmentUserCard(appointments = it, onClick = {}, onCancel = {
                    events(AppointmentEvents.OnCancelAppointment(it))
                })
            }
        }
    }
}


