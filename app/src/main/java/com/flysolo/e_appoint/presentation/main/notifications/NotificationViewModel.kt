package com.flysolo.e_appoint.presentation.main.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flysolo.e_appoint.repository.notification.NotificationRepository
import com.flysolo.e_appoint.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    var state by mutableStateOf(NotificationState())
    fun events(e : NotificationEvents) {
        when(e) {
            is NotificationEvents.OnGetNotifications -> getNotifications(e.userID)
            is NotificationEvents.OnSetUser -> state = state.copy(
                users = e.users
            )
        }
    }

    private fun getNotifications(userID: String) {
        viewModelScope.launch {
            notificationRepository.getAllNotificationByUser(userID) {
                state = when(it) {
                    is UiState.Error ->state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading ->state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        inboxes = it.data
                    )
                }
            }
        }

    }
}