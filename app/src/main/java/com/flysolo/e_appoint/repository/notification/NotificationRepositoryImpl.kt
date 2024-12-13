package com.flysolo.e_appoint.repository.notification

import android.provider.Telephony
import com.flysolo.e_appoint.models.appointments.Inbox
import com.flysolo.e_appoint.repository.appointment.INBOX_COLLECTION
import com.flysolo.e_appoint.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class NotificationRepositoryImpl(
    private val firestore: FirebaseFirestore
): NotificationRepository {
    override suspend fun updateStatus(inboxID: String): Result<String> {
        return try {
            firestore.collection(INBOX_COLLECTION)
                .document(inboxID)
                .update("seen", true)
                .await()
            Result.success("Status updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllNotificationByUser(
        userID: String,
        result: (UiState<List<Inbox>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
       firestore.collection(INBOX_COLLECTION)
           .whereEqualTo("userID",userID)
           .orderBy("createdAt",Query.Direction.DESCENDING)
           .addSnapshotListener { value, error ->
               value?.let {
                   result(UiState.Success(it.toObjects(Inbox::class.java)))
               }
               error?.let {
                   result.invoke(UiState.Error(it.localizedMessage.toString()))
               }
           }
    }
}