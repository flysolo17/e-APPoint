package com.flysolo.e_appoint.repository.appointment

import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.utils.UiState

interface AppointmentRepository {
    suspend fun createAppointment(appointment: Appointments): Result<String>
    suspend fun confirmAppointment(
        appointmentID: String
    ) : Result<String>

    suspend fun cancelAppointment(
        appointmentID: String
    ) : Result<String>


    suspend fun declineAppointment(
        appointmentID: String
    ) : Result<String>

    suspend fun markAppointmentDone(
        appointmentID: String
    ) : Result<String>


    suspend fun getAppointmentById(appointmentId: String): Result<Appointments>

    suspend fun getAppointmentsByUser(userId: String,result: (UiState<List<Appointments>>) -> Unit)

    suspend fun deleteAppointment(appointmentId: String): Result<Boolean>

    suspend fun getAllAppointments(result: (UiState<List<Appointments>>) -> Unit)
}