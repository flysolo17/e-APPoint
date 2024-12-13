package com.flysolo.e_appoint.presentation.main.home

import com.flysolo.e_appoint.models.users.Users
import kotlin.math.min


data class HomeState(
    val isLoading : Boolean = false,
    val users : Users ? = null,
    val errors : String ? = null,
    val messages : String ? = null
)