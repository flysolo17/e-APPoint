package com.flysolo.e_appoint.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.models.users.UserType
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.presentation.auth.change_password.ChangePasswordScreen
import com.flysolo.e_appoint.presentation.auth.change_password.ChangePasswordViewModel
import com.flysolo.e_appoint.presentation.auth.edit_profile.EditProfileScreen
import com.flysolo.e_appoint.presentation.auth.edit_profile.EditProfileViewModel
import com.flysolo.e_appoint.presentation.drawer.about.AboutScreen
import com.flysolo.e_appoint.presentation.drawer.contact.ContactScreen
import com.flysolo.e_appoint.presentation.drawer.help.HelpScreen
import com.flysolo.e_appoint.presentation.drawer.personal_info.InfoEvents
import com.flysolo.e_appoint.presentation.drawer.personal_info.InfoState
import com.flysolo.e_appoint.presentation.drawer.personal_info.InfoViewModel
import com.flysolo.e_appoint.presentation.drawer.personal_info.PersonalInfoScreen
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentEvents
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentViewModel
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentsScreen
import com.flysolo.e_appoint.presentation.main.create_appointment.CreateAppointmentEvents
import com.flysolo.e_appoint.presentation.main.create_appointment.CreateAppointmentScreen
import com.flysolo.e_appoint.presentation.main.create_appointment.CreateAppointmentViewModel
import com.flysolo.e_appoint.presentation.main.dashboard.DashboardScreen
import com.flysolo.e_appoint.presentation.main.dashboard.DashboardViewModel
import com.flysolo.e_appoint.presentation.main.home.HomeEvents
import com.flysolo.e_appoint.presentation.main.home.HomeScreen
import com.flysolo.e_appoint.presentation.main.home.HomeViewModel
import com.flysolo.e_appoint.presentation.main.notifications.NotificationEvents
import com.flysolo.e_appoint.presentation.main.notifications.NotificationScreen
import com.flysolo.e_appoint.presentation.main.notifications.NotificationViewModel

import com.flysolo.e_appoint.presentation.main.profile.ProfileScreen
import com.flysolo.e_appoint.presentation.main.profile.ProfileViewModel
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainNavGraph
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    mainNavController : NavHostController,
    navHostController: NavHostController,
    user: Users?,
) {
    NavHost(navController = navHostController, startDestination = AppRouter.HOME.route) {
        composable(
            route = AppRouter.HOME.route
        ) {
            if (user?.type == UserType.ADMIN) {
                val viewModel = hiltViewModel<DashboardViewModel>()
//                viewModel.events(HomeEvents.OnSetUser(user))
                DashboardScreen(
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController
                )

            } else {
                val viewModel = hiltViewModel<HomeViewModel>()
                viewModel.events(HomeEvents.OnSetUser(user))
                HomeScreen(
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController
                )
            }

        }
        composable(
            route = AppRouter.APPOINTMENTS.route
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            viewModel.events(AppointmentEvents.OnSetUser(user))
            AppointmentsScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.PROFILE.route
        ) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
                mainNavController = mainNavController
            )
        }


        composable(
            route = AppRouter.CHANGE_PASSWORD.route
        ) {
            val viewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePasswordScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
            )
        }

        composable(
            route = AppRouter.EDIT_PROFILE.route,

            ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val users = Gson().fromJson(decodedJson, Users::class.java)
            val viewModel = hiltViewModel<EditProfileViewModel>()
            users?.let {
                EditProfileScreen(
                    users = it,
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController
                )
            }
        }

        composable(route = AppRouter.CREATE_APPOINTMENT.route)  {
            val viewModel = hiltViewModel<CreateAppointmentViewModel>()
            viewModel.events(CreateAppointmentEvents.OnSetUser(user))
            CreateAppointmentScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(route = AppRouter.NOTIFICATIONS.route)  {
            val viewModel = hiltViewModel<NotificationViewModel>()
            viewModel.events(NotificationEvents.OnSetUser(user))
            NotificationScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(route = AppRouter.PERSONAL_INFO.route) {
            val viewModel = hiltViewModel<InfoViewModel>()
            viewModel.events(InfoEvents.OnSetUsers(user))
            PersonalInfoScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(route = AppRouter.CONTACT_AND_SECURITY.route) {
            ContactScreen(navHostController = navHostController)
        }
        composable(route = AppRouter.HELP_CENTER.route) {
            HelpScreen(navHostController = navHostController)
        }

        composable(route = AppRouter.ABOUT_US.route) {
            AboutScreen(navHostController = navHostController)
        }
    }
}