package com.flysolo.e_appoint.presentation.auth.login


sealed interface LoginEvents  {

    data class OnEmailChange(val email : String) : LoginEvents
    data class OnPasswordChange(val password : String) : LoginEvents
    data object OnTogglePasswordVisibility : LoginEvents

    data object OnGetCurrentUser : LoginEvents

    data object OnLogin : LoginEvents
}