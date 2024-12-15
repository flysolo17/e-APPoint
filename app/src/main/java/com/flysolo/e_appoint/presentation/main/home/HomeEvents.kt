package com.flysolo.e_appoint.presentation.main.home

import com.flysolo.e_appoint.models.users.Users


sealed interface HomeEvents {
    data class OnSetUser(
        val users: Users ?
    ) : HomeEvents


    data class OnGetAppointments(val userID : String ) : HomeEvents

    data class OnCancel(val appointmentID : String) : HomeEvents
}