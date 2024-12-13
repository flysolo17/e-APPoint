package com.flysolo.e_appoint.presentation.auth.login

import com.flysolo.e_appoint.models.users.UserWithVerification
import com.flysolo.e_appoint.utils.TextFieldData


data class LoginState(
    val isLoading : Boolean = false,
    val isSigningWithGoogle : Boolean = false,
    val user: UserWithVerification? = null,
    val errors : String ? = null,
    val email : TextFieldData = TextFieldData(),
    val password : TextFieldData = TextFieldData(),
    val isPasswordVisible : Boolean = false,


    )