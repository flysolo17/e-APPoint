package com.flysolo.e_appoint.presentation.auth.edit_profile

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.TextFieldValue
import com.flysolo.e_appoint.utils.TextFieldData


data class EditProfileState(
    val isLoading : Boolean = false,
    val name : TextFieldData = TextFieldData(),
    val phone : TextFieldData = TextFieldData(),
    val errors : String ? = null,
    val isSaving : Boolean = false,
    val isDoneSaving : String ? = null
)