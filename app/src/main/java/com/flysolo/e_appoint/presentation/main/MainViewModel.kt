package com.flysolo.e_appoint.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
    init {
        events(MainEvents.OnGetCurrentUser)
    }
    fun events(e : MainEvents) {
        when(e) {
            MainEvents.OnGetAllAppointments -> getAppointments()
            MainEvents.OnGetCurrentUser -> getUser()
            MainEvents.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    private fun getAppointments() {

    }

    private fun getUser() {
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