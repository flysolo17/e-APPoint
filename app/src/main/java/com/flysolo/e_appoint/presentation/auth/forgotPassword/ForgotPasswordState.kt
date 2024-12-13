package com.flysolo.e_appoint.presentation.auth.forgotPassword

import com.flysolo.e_appoint.utils.TextFieldData


data class ForgotPasswordState(
    val isLoading : Boolean = false,
    val isSent : String ? = null,
    val errors : String ? = null,
    val email : TextFieldData = TextFieldData()
)