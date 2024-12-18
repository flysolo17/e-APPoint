package com.flysolo.e_appoint.presentation.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.repository.appointment.AppointmentRepository
import com.flysolo.e_appoint.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(HomeState())
    fun events(e : HomeEvents) {
        when(e) {
            is HomeEvents.OnSetUser -> state = state.copy(
                users = e.users
            )

            is HomeEvents.OnGetAppointments -> getAppointments(e.userID)
            is HomeEvents.OnCancel -> cancel(e.appointmentID)
        }
    }

    private fun cancel(appointmentID: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            appointmentRepository.cancelAppointment(appointmentID).onSuccess {
                state = state.copy(
                    isLoading = false,
                    errors = null,
                    messages = it.toString()
                )
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    errors = it.localizedMessage?.toString(),
                )
            }
            delay(1000)
            state = state.copy(
                messages = null
            )
        }
    }

    private fun getAppointments(userID: String) {
        viewModelScope.launch {
            appointmentRepository.getAppointmentsByUser(userID) {
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
                        errors = null,
                        appointments = it.data
                    )
                }
            }
        }
    }
}