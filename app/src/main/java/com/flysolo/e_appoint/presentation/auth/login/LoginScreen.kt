package com.flysolo.e_appoint.presentation.auth.login

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.utils.defaultColors

import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    events: (LoginEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current


    LaunchedEffect(state) {
        if (state.user != null) {
            val route = if (state.user.isVerified) AppRouter.MAIN.route else AppRouter.VERIFICATION.route
            navHostController.navigate(route) {
                popUpTo(AppRouter.LOGIN.route) { inclusive = true }
            }
        }
    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = modifier.size(200.dp)
            )
            Spacer(
                modifier = modifier.height(height = 16.dp)
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text(stringResource(R.string.email)) },
                value = state.email.value,
                onValueChange = {events(LoginEvents.OnEmailChange(it))},
                isError = state.email.hasError,
                maxLines = 1,
                shape = MaterialTheme.shapes.medium,
                supportingText = {
                    Text(
                        state.email.errorMessage ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.error
                    ))
                }
            )

            TextField(
                modifier = modifier.fillMaxWidth(),
                colors = TextFieldDefaults.defaultColors(),
                placeholder = { Text(stringResource(R.string.password)) },
                value = state.password.value,
                onValueChange = { events(LoginEvents.OnPasswordChange(it)) },
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
                    IconButton(onClick = { events(LoginEvents.OnTogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (state.isPasswordVisible) {
                                stringResource(R.string.show)
                            } else stringResource(R.string.hide)
                        )
                    }
                }
            )

            TextButton(
                modifier = modifier.align(Alignment.End),
                onClick = { navHostController.navigate(AppRouter.FORGOT_PASSWORD.route) }) { Text(
                stringResource(R.string.forgot_password)
            ) }
            Spacer(
                modifier = modifier.height(8.dp)
            )
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {events.invoke(LoginEvents.OnLogin)},
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
                        Text(stringResource(R.string.sign_in))
                    }
                }
            }
            Spacer(
                modifier = modifier.height(12.dp)
            )
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                onClick = {     navHostController.navigate(AppRouter.REGISTER.route)}
            ) {
                Text("Create Account",modifier = modifier.padding(8.dp))
            }
        }
    }
}


