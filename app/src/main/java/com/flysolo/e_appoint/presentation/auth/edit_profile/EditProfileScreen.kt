package com.flysolo.e_appoint.presentation.auth.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.presentation.auth.register.FormControl
import com.flysolo.e_appoint.presentation.auth.register.RegisterEvents
import com.flysolo.e_appoint.utils.BackButton
import com.flysolo.e_appoint.utils.defaultColors

import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    users: Users,
    state: EditProfileState,
    events: (EditProfileEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(key1 = users) {
        events.invoke(EditProfileEvents.OnNameChange(users.name?:""))
        events.invoke(EditProfileEvents.OnPhoneChange(users.phone?:""))

    }
    val context = LocalContext.current
    LaunchedEffect(key1 = state) {
        if (state.errors != null) {
            Toast.makeText(
                context,
                state.errors,
                Toast.LENGTH_SHORT
            ).show()
        }
        if (state.isDoneSaving != null) {

            Toast.makeText(
                context,
                state.isDoneSaving,
                Toast.LENGTH_SHORT
            ).show()
            delay(1000)
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
                    Text("Change Password")
                },
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
        ){
            Text(text = "* Required", color = MaterialTheme.colorScheme.error)
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = state.name.value,
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text(stringResource(R.string.fullname)) },
                onValueChange = {events(EditProfileEvents.OnNameChange(it))},
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
                onValueChange = {events(EditProfileEvents.OnPhoneChange(it))},
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
            Spacer(modifier = modifier.weight(1f))
            Button(
                shape = MaterialTheme.shapes.small,
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006400),
                    contentColor = Color.White
                ),
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {
                    events.invoke(EditProfileEvents.OnSaveChanges(
                        users.id ?: "",
                        name = state.name.value,
                        phone = state.phone.value
                    ))
                }
            ) {
                Row(
                    modifier = modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = modifier.size(18.dp).padding(end = 8.dp)
                        )
                    }
                    Text(
                        "Save Profile",
                    )
                }

            }
        }
    }

}