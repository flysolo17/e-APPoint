package com.flysolo.e_appoint.presentation.main.profile

import android.net.Uri


sealed interface ProfileEvents  {
    data object OnGetCurrentUser : ProfileEvents
    data object OnLoggedOut : ProfileEvents
    data class OnDeleteAccount(
        val password : String
    ): ProfileEvents

    data class SelectProfile(val uri : Uri) : ProfileEvents
}