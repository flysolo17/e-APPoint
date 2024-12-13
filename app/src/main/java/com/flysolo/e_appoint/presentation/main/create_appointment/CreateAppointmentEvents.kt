package com.flysolo.e_appoint.presentation.main.create_appointment

import com.flysolo.e_appoint.models.users.Users


sealed interface CreateAppointmentEvents {
    data class OnSetUser(val users: Users ? ) : CreateAppointmentEvents
    data object OnSubmit : CreateAppointmentEvents


    data class OnNoteChange(val note : String): CreateAppointmentEvents

    data class OnSelectService(
        val  service : String
    ) : CreateAppointmentEvents

    data class OnNameChange(
        val  name : String
    ) : CreateAppointmentEvents


    data class OnPhoneChange(
        val  phone : String
    ) : CreateAppointmentEvents

    data class OnDateChange(
        val  date : String
    ) : CreateAppointmentEvents

    data class OnTimeChange(
        val  time : String
    ) : CreateAppointmentEvents


}