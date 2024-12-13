package com.flysolo.e_appoint.presentation.main.create_appointment

import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.utils.TextFieldData


data class CreateAppointmentState(
    val isLoading : Boolean = false,
    val users : Users ? = null,
    val errors : String ? = null,
    val created:  String  ? = null,


    //create appointment form
    val selectedService : String ? = null,
    val notes :  String  = "",
    val name :  TextFieldData  = TextFieldData(),
    val phone : TextFieldData  = TextFieldData(),

    val date : String = "",
    val time : String = ""



)