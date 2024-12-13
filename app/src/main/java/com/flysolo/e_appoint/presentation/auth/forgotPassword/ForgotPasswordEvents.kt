package com.flysolo.e_appoint.presentation.auth.forgotPassword



sealed interface ForgotPasswordEvents {
    data class OnResetPassword(val email : String) : ForgotPasswordEvents
    data class OnEmailChaged(val email : String) : ForgotPasswordEvents
}