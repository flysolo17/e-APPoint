package com.flysolo.e_appoint.presentation.main.notifications

import com.flysolo.e_appoint.models.users.Users


sealed interface NotificationEvents {
    data class OnGetNotifications(
        val userID :String
    ) : NotificationEvents

    data class OnSetUser(
        val users : Users?
    ) : NotificationEvents
}