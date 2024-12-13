package com.flysolo.e_appoint.presentation.auth.register

import com.flysolo.e_appoint.utils.TextFieldData


data class RegisterState(
    val isLoading : Boolean = false,
    val name : TextFieldData =TextFieldData(),
    val phone : TextFieldData =TextFieldData(),
    val email : TextFieldData =TextFieldData(),
    val password : TextFieldData =TextFieldData(),
    val confirmPassword: TextFieldData =TextFieldData(),
    val isPasswordVisible : Boolean = false,
    val isConfirmPasswordVisible : Boolean = false,
    val errors : String ? = null,
    val registeredMessage : String ? = null
)