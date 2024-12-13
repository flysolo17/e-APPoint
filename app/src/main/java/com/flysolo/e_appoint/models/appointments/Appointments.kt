package com.flysolo.e_appoint.models.appointments

import java.util.Date
import java.util.UUID


data class Appointments(
    val id : String ? = null,
    val userID : String? = null,
    val clientInfo: ClientInfo ? = null,
    val service : String? = null,
    val schedules: Schedules ? = null,
    val status : AppointmentStatus = AppointmentStatus.PENDING,
    val note : String ?= null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date()
)

data class ClientInfo(
    val name : String ? = "Unknown User",
    val phone : String ? = "00000000000"
)
data class Schedules(
    val date : String ? = null,
    val time : String ? = null,
)




enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    DONE,
    CANCELLED,
    DECLINED
}

fun AppointmentStatus.createInboxMessage(userID: String?): Inbox {
    val message = when (this) {
        AppointmentStatus.PENDING -> "Your appointment request is pending. Please wait for confirmation."
        AppointmentStatus.CONFIRMED -> "Your appointment has been confirmed. We look forward to seeing you!"
        AppointmentStatus.DONE -> "Thank you for attending your appointment. We hope everything went well."
        AppointmentStatus.CANCELLED -> "Your appointment has been cancelled. Please contact us for rescheduling."
        AppointmentStatus.DECLINED -> "Your appointment request has been declined. Please contact us for further assistance."
    }

    return Inbox(
        id = UUID.randomUUID().toString(),
        status = this,
        message = message,
        userID = userID,
        createdAt = Date(),
        seen = false
    )
}
