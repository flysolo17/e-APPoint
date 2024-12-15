package com.flysolo.e_appoint.presentation.main.dashboard

import com.flysolo.e_appoint.models.appointments.Appointments


data class DashboardState(
    val isLoading : Boolean = false,
    val appointments: List<Appointments> = emptyList(),
    val errors : String ? = null,

    val messages : String? = null,
)