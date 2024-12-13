package com.flysolo.e_appoint.models.appointments

import java.util.Date


data class Inbox(
    val id : String ? = null,
    val status :  AppointmentStatus ? = null,
    val message :  String ? = null,
    val userID : String ? = null,
    val createdAt : Date = Date(),
    val seen : Boolean = false
)
