package com.flysolo.e_appoint.presentation.main.notifications

import com.flysolo.e_appoint.models.appointments.Inbox
import com.flysolo.e_appoint.models.users.Users


data class NotificationState(
    val isLoading : Boolean = false,
    val users: Users ? = null,
    val inboxes : List<Inbox> = emptyList(),
    val errors : String ? = null
)