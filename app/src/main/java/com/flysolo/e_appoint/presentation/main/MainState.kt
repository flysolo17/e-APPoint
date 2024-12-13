package com.flysolo.e_appoint.presentation.main

import com.flysolo.e_appoint.models.users.Users


data class MainState(
    val isLoading : Boolean =false,
    val users: Users ? = null,
    val errors : String ? = null
)