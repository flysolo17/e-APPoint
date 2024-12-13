package com.flysolo.e_appoint.presentation.auth.edit_profile


sealed interface EditProfileEvents  {

    data class OnNameChange(val name : String) : EditProfileEvents

    data class OnPhoneChange(val phone : String) : EditProfileEvents


    data class OnSaveChanges(
        val uid : String,
        val name : String,
        val phone : String,

    ) : EditProfileEvents
}