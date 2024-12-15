package com.flysolo.e_appoint.presentation.drawer.personal_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class InfoViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(InfoState())
    fun events(e :  InfoEvents) {
        when(e) {
            is InfoEvents.OnSetUsers -> state = state.copy(
                users = e.users
            )
        }
    }
}