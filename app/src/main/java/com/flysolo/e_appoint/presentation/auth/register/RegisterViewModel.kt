package com.flysolo.e_appoint.presentation.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.models.users.UserType
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
    fun events(e : RegisterEvents) {
        when(e) {
            is RegisterEvents.OnFormChange -> formChange(e.text,e.control)
            RegisterEvents.OnRegister -> submit()
            RegisterEvents.OnToggleConfirmPasswordVisibility -> state = state.copy(
                isConfirmPasswordVisible = !state.isConfirmPasswordVisible
            )
            RegisterEvents.OnTogglePasswordVisibility -> state = state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private fun formChange(text: String, control: FormControl) {
        val isValid: Boolean
        val errorMessage: String?

        when (control) {
            FormControl.NAME -> {
                isValid = text.isNotBlank()
                errorMessage = if (isValid) null else "Name cannot be empty"
                state = state.copy(
                    name = state.name.copy(
                        value = text,
                        hasError = isValid,
                        errorMessage = errorMessage
                    )
                )
            }
            FormControl.PHONE -> {
                //starts with 09 lenght 11
                isValid = text.matches("^09\\d{9}\$".toRegex())
                errorMessage = if (isValid) null else "Phone number must start with '09' and have 11 digits"
                state = state.copy(
                    phone = state.phone.copy(
                        value = text,
                        hasError = !isValid, // `hasError` is true when `isValid` is false
                        errorMessage = errorMessage
                    )
                )
            }
            FormControl.EMAIL -> {
                isValid = text.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()) // Basic email validation
                errorMessage = if (isValid) null else "Invalid email address"
                state = state.copy(
                    email = state.email.copy(
                        value = text,
                        hasError = isValid,
                        errorMessage = errorMessage
                    )
                )
            }
            FormControl.PASSWORD -> {
                isValid = text.length >= 6
                errorMessage = if (isValid) null else "Password must be at least 6 characters"
                state = state.copy(
                    password = state.password.copy(
                        value = text,
                        hasError = isValid,
                        errorMessage = errorMessage
                    )
                )
            }
            FormControl.CONFIRM_PASSWORD -> {
                isValid = text == state.password.value
                errorMessage = if (isValid) null else "Passwords do not match"
                state = state.copy(
                    confirmPassword = state.confirmPassword.copy(
                        value = text,
                        hasError = isValid,
                        errorMessage = errorMessage
                    )
                )
            }
        }
    }

    private fun submit() {
        val users : Users = Users(
            name = state.name.value,
            phone = state.phone.value,
            email = state.email.value,
            type = UserType.CLIENT
        )
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            authRepository.register(
                users,
                state.confirmPassword.value
            ).onSuccess {
                state = state.copy(
                    isLoading = false,
                    registeredMessage = it
                )
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    errors = it.localizedMessage.toString()
                )
            }
        }
    }
}