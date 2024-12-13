package com.flysolo.e_appoint.presentation.main.create_appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.presentation.auth.register.FormControl
import com.flysolo.e_appoint.presentation.auth.register.RegisterEvents
import com.flysolo.e_appoint.utils.AppointmentDatePicker
import com.flysolo.e_appoint.utils.AppointmentTimePicker
import com.flysolo.e_appoint.utils.BackButton
import com.flysolo.e_appoint.utils.ServicesDropdown
import com.flysolo.e_appoint.utils.defaultColors
import com.flysolo.e_appoint.utils.shortToast
import kotlinx.coroutines.time.delay


val services = listOf(
    "Dental Check-up",
    "Oral Prophylaxis (Dental Cleaning)",
    "Tooth Extraction",
    "Temporary Restoration",
    "Medication"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppointmentScreen(
    modifier: Modifier = Modifier,
    state: CreateAppointmentState,
    events: (CreateAppointmentEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.errors) {
        state.errors?.let {
            context.shortToast(state.errors)
        }
    }
    LaunchedEffect(state.created) {
        state.created?.let {
            context.shortToast(state.created)
            kotlinx.coroutines.delay(1000)
            navHostController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    BackButton { navHostController.popBackStack() }

                },
                title = {
                    Text("Create Appointment")
                },
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize().padding(it).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Services", style = MaterialTheme.typography.titleLarge)
            ServicesDropdown(
                selectedService = state.selectedService,
                services = services,
                onServiceSelected = {
                    events.invoke(CreateAppointmentEvents.OnSelectService(it))
                }
            )
            Text("Schedule Info", style = MaterialTheme.typography.titleLarge)
            AppointmentDatePicker(value = state.date) {
                events.invoke(CreateAppointmentEvents.OnDateChange(it))
            }
            AppointmentTimePicker(
                initialTime = state.time
            ) {
                events(CreateAppointmentEvents.OnTimeChange(it))
            }

            Text("Client Info", style = MaterialTheme.typography.titleLarge)

            TextField(
                modifier = modifier.fillMaxWidth(),
                value = state.name.value,
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text(stringResource(R.string.fullname)) },
                onValueChange = {events(CreateAppointmentEvents.OnNameChange(it))},
                isError = state.name.hasError,
                maxLines = 1,
                shape = MaterialTheme.shapes.small,
                supportingText = {
                    Text(
                        state.name.errorMessage ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.error
                        ))
                }
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = state.phone.value,
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text("Example. 09887878657") },
                onValueChange = {events(CreateAppointmentEvents.OnPhoneChange(it))},
                isError = state.phone.hasError,
                maxLines = 1,
                shape = MaterialTheme.shapes.small,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                supportingText = {
                    Text(
                        state.phone.errorMessage ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.error
                        ))
                }
            )
            //add notes texfield

            // Add Notes TextField

            TextField(
                modifier = modifier.fillMaxWidth(),
                value = state.notes,
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text("Add Notes") },
                onValueChange = { events(CreateAppointmentEvents.OnNoteChange(it)) },
                 maxLines = 4,
                shape = MaterialTheme.shapes.small,

                supportingText = {
                    Text( "",)
                }
            )
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {events.invoke(CreateAppointmentEvents.OnSubmit)},
                enabled = !state.isLoading,
                shape = MaterialTheme.shapes.medium,
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Create Appointment")
                    }
                }
            }
        }
    }
}