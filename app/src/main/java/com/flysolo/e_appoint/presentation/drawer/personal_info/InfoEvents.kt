package com.flysolo.e_appoint.presentation.drawer.personal_info

import com.flysolo.e_appoint.models.users.Users


sealed interface InfoEvents {
    data class OnSetUsers(val users: Users ? ) : InfoEvents
}