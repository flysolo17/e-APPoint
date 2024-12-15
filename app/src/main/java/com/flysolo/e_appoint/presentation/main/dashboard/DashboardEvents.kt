package com.flysolo.e_appoint.presentation.main.dashboard

import com.flysolo.e_appoint.presentation.main.appointments.AppointmentEvents


sealed interface DashboardEvents {
    data object OnGetAllAppointments : DashboardEvents



    data class OnCancelAppointment(
        val appointmentID : String
    ) : DashboardEvents

    data class OnDeclineAppointment(
        val appointmentID: String
    ) : DashboardEvents

    data class OnConfirmAppointment(
        val appointmentID: String
    ) : DashboardEvents

    data class OnCompleteAppointment(
        val appointmentID: String
    ) : DashboardEvents
}