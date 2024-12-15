package com.flysolo.e_appoint.presentation.main.dashboard

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
class DashboardViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(DashboardState())
    init {
        events(DashboardEvents.OnGetAllAppointments)
    }
    fun events(e : DashboardEvents) {
        when(e) {
            DashboardEvents.OnGetAllAppointments -> getAppointments()
            is DashboardEvents.OnCancelAppointment -> cancelAppointment(appointmentID = e.appointmentID)
            is DashboardEvents.OnCompleteAppointment -> complete(e.appointmentID)
            is DashboardEvents.OnConfirmAppointment -> confirm(e.appointmentID)
            is DashboardEvents.OnDeclineAppointment -> decline(e.appointmentID)
        }
    }

    private fun getAppointments() {
        viewModelScope.launch {
            appointmentRepository.getAllAppointments {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = null,
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


    private fun decline(appointmentID: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            appointmentRepository.declineAppointment(appointmentID).onSuccess {
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

    private fun confirm(appointmentID: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            appointmentRepository.confirmAppointment(appointmentID).onSuccess {
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

    private fun complete(appointmentID: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            appointmentRepository.markAppointmentDone(appointmentID).onSuccess {
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

    private fun cancelAppointment(appointmentID: String) {
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
}