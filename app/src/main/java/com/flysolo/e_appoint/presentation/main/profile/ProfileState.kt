package com.flysolo.e_appoint.presentation.main.profile

import com.flysolo.e_appoint.models.users.Users


data class ProfileState(
    val isLoading : Boolean = false,
    val users: Users ? = null,
    val isLoggedOut : String ? = null,
    val errors : String ? = null,
    val messages : String ? = null
)