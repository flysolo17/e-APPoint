package com.flysolo.e_appoint.presentation.main.appointments

import com.flysolo.e_appoint.models.users.Users


sealed interface AppointmentEvents {
    data class OnSetUser(val users: Users ? ) : AppointmentEvents
    data object OnGetAllAppointments : AppointmentEvents
    data class OnGetMyAppointments(
        val userID : String
    ) : AppointmentEvents

    data class OnCancelAppointment(
        val appointmentID : String
    ) : AppointmentEvents

    data class OnDeclineAppointment(
        val appointmentID: String
    ) : AppointmentEvents

    data class OnConfirmAppointment(
        val appointmentID: String
    ) : AppointmentEvents

    data class OnCompleteAppointment(
        val appointmentID: String
    ) : AppointmentEvents
}