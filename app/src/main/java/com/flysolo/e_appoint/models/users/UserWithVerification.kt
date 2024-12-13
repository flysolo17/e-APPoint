package com.flysolo.e_appoint.models.users



data class UserWithVerification(
    val users: Users? = null,
    val isVerified : Boolean = false
)