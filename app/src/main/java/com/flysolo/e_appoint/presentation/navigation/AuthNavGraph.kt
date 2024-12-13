package com.flysolo.e_appoint.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.auth.forgotPassword.ForgotPasswordScreen
import com.flysolo.e_appoint.presentation.auth.forgotPassword.ForgotPasswordViewModel
import com.flysolo.e_appoint.presentation.auth.login.LoginScreen
import com.flysolo.e_appoint.presentation.auth.login.LoginViewModel
import com.flysolo.e_appoint.presentation.auth.register.RegisterScreen
import com.flysolo.e_appoint.presentation.auth.register.RegisterViewModel
import com.flysolo.e_appoint.presentation.auth.verification.VerificationScreen
import com.flysolo.e_appoint.presentation.auth.verification.VerificationViewModel


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = AppRouter.LOGIN.route,
        route = AppRouter.AUTH.route
    ) {
        composable(route = AppRouter.LOGIN.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navController
            )
        }

        composable(route = AppRouter.REGISTER.route) {
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navController
            )
        }

        composable(route = AppRouter.FORGOT_PASSWORD.route) {
            val viewModel = hiltViewModel<ForgotPasswordViewModel>()
            ForgotPasswordScreen(
                navHostController = navController,
                state = viewModel.state,
                events = viewModel::events
            )
        }
        composable(route = AppRouter.VERIFICATION.route) {
            val viewModel = hiltViewModel<VerificationViewModel>()
            VerificationScreen(
                navHostController = navController,
                state = viewModel.state,
                events = viewModel::events
            )
        }

    }
}
