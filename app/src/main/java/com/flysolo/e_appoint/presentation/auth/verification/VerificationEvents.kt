package com.flysolo.e_appoint.presentation.auth.verification

import android.content.Context


sealed interface VerificationEvents {
    data class OnSendVerification(val context : Context) : VerificationEvents
    data object OnListenToUserVerification : VerificationEvents
    data object OnGetCurrentUser : VerificationEvents
}