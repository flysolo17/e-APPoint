package com.flysolo.e_appoint.presentation.auth.register





sealed interface RegisterEvents {
    data object OnRegister  : RegisterEvents
    data class OnFormChange(
        val text : String,
        val control : FormControl
    ) : RegisterEvents

    data object OnTogglePasswordVisibility : RegisterEvents
    data object OnToggleConfirmPasswordVisibility : RegisterEvents
}


enum class FormControl {
    NAME ,
    PHONE,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD
}