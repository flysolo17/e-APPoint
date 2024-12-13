package com.flysolo.e_appoint.presentation.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.auth.login.LoginEvents
import com.flysolo.e_appoint.utils.defaultColors
import com.flysolo.e_appoint.utils.shortToast
import kotlinx.coroutines.time.delay


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterState,
    events: (RegisterEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.errors) {
        if (state.errors != null) {
            context.shortToast(state.errors)
        }
    }
    LaunchedEffect(state.registeredMessage) {

        if (state.registeredMessage != null) {
            context.shortToast(state.registeredMessage)
            navHostController.navigate(AppRouter.VERIFICATION.route)
        }
    }
    Scaffold(

    ){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            )
        {
            item {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = modifier.size(200.dp)
                )
            }

            item {
                Box(
                    modifier = modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Create an account", style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    ))
                }
            }
            //name
            item {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    value = state.name.value,
                    colors = TextFieldDefaults.defaultColors(),
                    placeholder = { Text(stringResource(R.string.fullname)) },
                    onValueChange = {events(RegisterEvents.OnFormChange(it,FormControl.NAME))},
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
            }

            //phone
            item {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    value = state.phone.value,
                    colors = TextFieldDefaults.defaultColors(),
                    placeholder = { Text("Example. 09887878657") },
                    onValueChange = {events(RegisterEvents.OnFormChange(it,FormControl.PHONE))},
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
            }

            //email
            item {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    value = state.email.value,
                    colors = TextFieldDefaults.defaultColors(),
                    placeholder = { Text(stringResource(R.string.email)) },
                    onValueChange = {events(RegisterEvents.OnFormChange(it,FormControl.EMAIL))},
                    isError = state.email.hasError,
                    maxLines = 1,
                    shape = MaterialTheme.shapes.small,
                    supportingText = {
                        Text(
                            state.email.errorMessage ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.error
                            ))
                    }
                )
            }

            //password
            item {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.defaultColors(),
                    placeholder = { Text(stringResource(R.string.password)) },
                    value = state.password.value,
                    onValueChange = { events(RegisterEvents.OnFormChange(it,FormControl.PASSWORD)) },
                    isError = state.password.hasError,
                    maxLines = 1,
                    shape = MaterialTheme.shapes.medium,
                    supportingText = {
                        Text(
                            text = state.password.errorMessage ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )

                    },
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { events(RegisterEvents.OnTogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (state.isPasswordVisible) {
                                    stringResource(R.string.show)
                                } else stringResource(R.string.hide)
                            )
                        }
                    }
                )
            }


            //confirm password
            item {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.defaultColors(),
                    placeholder = { Text(stringResource(R.string.password)) },
                    value = state.confirmPassword.value,
                    onValueChange = { events(RegisterEvents.OnFormChange(it,FormControl.CONFIRM_PASSWORD)) },
                    isError = state.confirmPassword.hasError,
                    maxLines = 1,
                    shape = MaterialTheme.shapes.medium,
                    supportingText = {
                        Text(
                            text = state.confirmPassword.errorMessage ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )

                    },
                    visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { events(RegisterEvents.OnToggleConfirmPasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (state.isConfirmPasswordVisible) {
                                    stringResource(R.string.show)
                                } else stringResource(R.string.hide)
                            )
                        }
                    }
                )
            }


            item {
                Spacer(
                    modifier = modifier.height(8.dp)
                )
            }

            //button register
            item {
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {events.invoke(RegisterEvents.OnRegister)},
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
                            Text(stringResource(R.string.create_account))
                        }
                    }
                }
            }
        }
    }
}