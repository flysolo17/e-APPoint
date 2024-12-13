package com.flysolo.e_appoint.repository.appointment

import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.models.appointments.createInboxMessage
import com.flysolo.e_appoint.repository.auth.APPOINTMENT_COLLECTION
import com.flysolo.e_appoint.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Date

const val INBOX_COLLECTION = "inbox"

class AppointmentRepositoryImpl(
    private val firestore: FirebaseFirestore
) : AppointmentRepository {

    override suspend fun createAppointment(appointment: Appointments): Result<String> {
        return try {
            val batch = firestore.batch()
            val inbox = appointment.status.createInboxMessage(appointment.userID)
            val appointmentRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointment.id ?: "")
            batch.set(appointmentRef, appointment)
            val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
            batch.set(inboxRef, inbox)
            batch.commit().await()
            Result.success("Appointment Created")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun confirmAppointment(appointmentID: String): Result<String> {
        return try {
            val appointmentRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointmentID)
            appointmentRef.update("status", AppointmentStatus.CONFIRMED, "updatedAt", Date()).await()

            val inbox = AppointmentStatus.CONFIRMED.createInboxMessage(null)
            firestore.collection(INBOX_COLLECTION).document(inbox.id!!).set(inbox).await()

            Result.success("Appointment Confirmed")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun cancelAppointment(appointmentID: String): Result<String> {
        return try {
            val appointmentRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointmentID)
            appointmentRef.update("status", AppointmentStatus.CANCELLED, "updatedAt", Date()).await()

            val inbox = AppointmentStatus.CANCELLED.createInboxMessage(null)
            firestore.collection(INBOX_COLLECTION).document(inbox.id!!).set(inbox).await()

            Result.success("Appointment Cancelled")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun declineAppointment(appointmentID: String): Result<String> {
        return try {
            val appointmentRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointmentID)
            appointmentRef.update("status", AppointmentStatus.DECLINED, "updatedAt", Date()).await()

            val inbox = AppointmentStatus.DECLINED.createInboxMessage(null)
            firestore.collection(INBOX_COLLECTION).document(inbox.id!!).set(inbox).await()

            Result.success("Appointment Declined")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAppointmentDone(appointmentID: String): Result<String> {
        return try {
            val appointmentRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointmentID)
            appointmentRef.update("status", AppointmentStatus.DONE, "updatedAt", Date()).await()

            val inbox = AppointmentStatus.DONE.createInboxMessage(null)
            firestore.collection(INBOX_COLLECTION).document(inbox.id!!).set(inbox).await()

            Result.success("Appointment Marked as Done")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAppointmentById(appointmentId: String): Result<Appointments> {
        return try {
            val snapshot = firestore.collection(APPOINTMENT_COLLECTION).document(appointmentId).get().await()
            val appointment = snapshot.toObject(Appointments::class.java)
                ?: throw Exception("Appointment not found")
            Result.success(appointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAppointmentsByUser(
        userId: String,
        result: (UiState<List<Appointments>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereEqualTo("userID", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.localizedMessage.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Appointments::class.java)))
                }
            }
    }

    override suspend fun deleteAppointment(appointmentId: String): Result<Boolean> {
        return try {
            firestore.collection(APPOINTMENT_COLLECTION).document(appointmentId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllAppointments(result: (UiState<List<Appointments>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(APPOINTMENT_COLLECTION)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.localizedMessage.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Appointments::class.java)))
                }
            }
    }
}
