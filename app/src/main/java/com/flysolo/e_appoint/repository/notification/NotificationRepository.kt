package com.flysolo.e_appoint.repository.notification


import com.flysolo.e_appoint.models.appointments.Inbox
import com.flysolo.e_appoint.utils.UiState


interface NotificationRepository  {
   suspend fun getAllNotificationByUser(userID : String,result : (UiState<List<Inbox>>) -> Unit)
   suspend fun updateStatus(inboxID : String) : Result<String>
}