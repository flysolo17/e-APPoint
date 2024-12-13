package com.flysolo.e_appoint.presentation.main.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.repository.auth.AuthRepository
import com.flysolo.e_appoint.utils.Password
import com.flysolo.e_appoint.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
    init {
        events(ProfileEvents.OnGetCurrentUser)
    }
    fun events(e : ProfileEvents) {
        when(e){
            ProfileEvents.OnGetCurrentUser -> getCurrentUser()
            ProfileEvents.OnLoggedOut -> logout()
            is ProfileEvents.OnDeleteAccount ->deleteAccount(e.password)
            is ProfileEvents.SelectProfile -> select(e.uri)
        }
    }

    private fun select(uri: Uri) {
        viewModelScope.launch {
            state.users?.id?.let {
                authRepository.changeProfile(it,uri) {
                    state = when(it) {
                        is UiState.Error -> state.copy(
                            errors = it.message,
                            isLoading = false
                        )
                        UiState.Loading -> state.copy(
                            isLoading = true,
                            errors = null
                        )
                        is UiState.Success -> state.copy(
                            isLoading = false,
                            errors = null,
                            messages = it.data
                        )
                    }
                }
            }
            delay(1000)
            state = state.copy(
                messages = null
            )
        }
    }

    private fun deleteAccount(password: String) {
        viewModelScope.launch {
            authRepository.deleteAccount(state.users?.id ?: "",password) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )

                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )

                    is UiState.Success -> state.copy(
                        isLoading = false,
                        isLoggedOut = it.data
                    )
                }
            }

        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            state = state.copy(
                isLoggedOut = "Successfully Logged out!"
            )
        }
    }

    private fun getCurrentUser() {

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.getCurrentUser().onSuccess {
                state = state.copy(
                    isLoading = false,
                    users = it?.users
                )
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    errors = it.localizedMessage?.toString()
                )
            }
        }
    }
}