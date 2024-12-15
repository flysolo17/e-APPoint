package com.flysolo.e_appoint.presentation.main



sealed interface MainEvents {
    data object OnGetCurrentUser : MainEvents
    data object OnGetAllAppointments : MainEvents

    data object Logout : MainEvents
}