package com.flysolo.e_appoint.presentation.main.home

import com.flysolo.e_appoint.models.users.Users


sealed interface HomeEvents {
    data class OnSetUser(
        val users: Users ?
    ) : HomeEvents

}