package com.flysolo.e_appoint.presentation.auth.verification

import com.flysolo.e_appoint.models.users.Users


data class VerificationState(
    val isLoading : Boolean = false,
    val timer : Int = 0,
    val errors : String ? = null,
    val isVerified: Boolean = false,
    val users: Users? = null,
)