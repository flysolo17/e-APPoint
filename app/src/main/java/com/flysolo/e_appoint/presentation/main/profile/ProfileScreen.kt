package com.flysolo.e_appoint.presentation.main.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.auth.change_password.ChangePasswordEvents
import com.flysolo.e_appoint.presentation.navigation.MainNavGraph
import com.flysolo.e_appoint.utils.Avatar
import com.flysolo.e_appoint.utils.ErrorScreen
import com.flysolo.e_appoint.utils.shortToast
import kotlinx.coroutines.delay


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state  : ProfileState,
    events: (ProfileEvents) -> Unit,
    navHostController: NavHostController,
    mainNavController : NavHostController
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events(ProfileEvents.SelectProfile(it))
        }
    }
    if (showDialog) {
        DeleteAccountConfirmationDialog(
            onConfirm = {
                showDialog = false
                events.invoke(ProfileEvents.OnDeleteAccount(it))
            },
            onDismiss = { showDialog = false }
        )
    }
    LaunchedEffect(state.errors) {
        state.errors?.let {
            context.shortToast(state.errors)
        }
    }
    LaunchedEffect(state.isLoggedOut) {
        state.isLoggedOut?.let {
            context.shortToast(state.isLoggedOut)
            delay(1000)
            mainNavController.navigate(AppRouter.AUTH.route)
        }
    }
    when {
        state.isLoading -> CircularProgressIndicator()
        state.errors != null -> ErrorScreen(message = state.errors) {
            Button(
                onClick = {navHostController.popBackStack()}
            ) { Text("Back") }
        }
        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val user = state.users
                Avatar(
                    url = user?.profile ?: "",
                    size = 150.dp
                ) {            imagePickerLauncher.launch("image/*") }
                Text("${user?.name}", style = MaterialTheme.typography.titleLarge)

                Text("${user?.email}", style = MaterialTheme.typography.labelMedium)
                Text("${user?.phone}", style = MaterialTheme.typography.labelSmall)
                Spacer(
                    modifier = modifier.weight(1f)
                )
                Button(
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006400),
                        contentColor = Color.White
                    ),
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = {
                        user?.let {
                            navHostController.navigate(AppRouter.EDIT_PROFILE.navigate(it))
                        }
                    }
                ) {
                    Text(
                        "Edit Profile",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                OutlinedButton(
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF006400)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF006400)),
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        navHostController.navigate(AppRouter.CHANGE_PASSWORD.route)
                    }) {
                    Text("Change Password",modifier = modifier.padding(8.dp))
                }


                Button(
                    shape = MaterialTheme.shapes.small,

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        showDialog = true
                    }) {
                        Text("Delete Account",modifier = modifier.padding(8.dp))
                }
                OutlinedButton(
                    shape = MaterialTheme.shapes.small,
                    modifier = modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.errorContainer),
                    onClick = {
                        events.invoke(ProfileEvents.OnLoggedOut)
                    }
                ) {
                    Text(
                        text = "Logout",
                        modifier = Modifier.padding(8.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun DeleteAccountConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit,  // Pass password to the onConfirm callback
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Are you sure you want to delete your account? If so, all your appointments will be cancelled. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Password input field
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = it.isEmpty()  // Show error if password is empty
                    },
                    label = { Text("Enter your password") },
                    isError = passwordError,

                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Password") }
                )
                if (passwordError) {
                    Text(
                        text = "Password cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (password.isNotEmpty()) {
                        onConfirm(password) // Pass password to the callback
                    } else {
                        passwordError = true
                    }
                }
            ) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        }
    )
}
