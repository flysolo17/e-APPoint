package com.flysolo.e_appoint.presentation.main.create_appointment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.models.appointments.Appointments
import com.flysolo.e_appoint.models.appointments.ClientInfo
import com.flysolo.e_appoint.models.appointments.Schedules
import com.flysolo.e_appoint.repository.appointment.AppointmentRepository
import com.flysolo.e_appoint.utils.generateRandomNumberString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(CreateAppointmentState())
    fun events(e :  CreateAppointmentEvents) {
        when(e) {
            is CreateAppointmentEvents.OnSetUser -> state = state.copy(
                users = e.users,
                name = state.name.copy(
                    value = e.users?.name ?: "unknown user"
                ),
                phone =  state.phone.copy(
                    value = e.users?.phone ?: "09000000000"
                ),
            )
            CreateAppointmentEvents.OnSubmit -> submit()
            is CreateAppointmentEvents.OnDateChange -> state = state.copy(
                date = e.date
            )
            is CreateAppointmentEvents.OnNameChange ->  {
                val isValid = e.name.isNotBlank()
                val errorMessage = if (isValid) null else "Name cannot be empty"
                state = state.copy(
                    name = state.name.copy(
                        value = e.name,
                        hasError = isValid,
                        errorMessage = errorMessage
                    )
                )
            }
            is CreateAppointmentEvents.OnPhoneChange -> {
                val isValid = e.phone.matches("^09\\d{9}\$".toRegex())
                val errorMessage = if (isValid) null else "Phone number must start with '09' and have 11 digits"
                state = state.copy(
                    phone = state.phone.copy(
                        value = e.phone,
                        hasError = !isValid,
                        errorMessage = errorMessage
                    )
                )
            }
            is CreateAppointmentEvents.OnSelectService ->  state = state.copy(
                selectedService = e.service
            )
            is CreateAppointmentEvents.OnTimeChange ->  state = state.copy(
                time = e.time
            )

            is CreateAppointmentEvents.OnNoteChange -> state = state.copy(
                notes = e.note
            )
        }
    }

    private fun submit() {
        val appointment = Appointments(
            id = generateRandomNumberString(8),
            userID = state.users?.id,
            service = state.selectedService,
            schedules = Schedules(
                date = state.date,
                time = state.time
            ),
            clientInfo = ClientInfo(
                name = state.name.value,
                phone = state.phone.value
            ),
            note = state.notes
        )
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            appointmentRepository.createAppointment(appointment).onSuccess {
                state = state.copy(
                    isLoading = false,
                    created = it
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