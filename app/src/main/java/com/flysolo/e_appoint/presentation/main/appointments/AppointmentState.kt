package com.flysolo.e_appoint.presentation.main.appointments

import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.models.users.Users


data class AppointmentState(
    val isLoading : Boolean = false,
    val appointments : List<Appointments> = emptyList(),
    val users : Users ? = null,
    val errors : String? = null,
    val messages : String ? = null
)