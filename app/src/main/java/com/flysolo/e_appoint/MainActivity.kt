package com.flysolo.e_appoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.main.MainScreen
import com.flysolo.e_appoint.presentation.main.MainViewModel
import com.flysolo.e_appoint.presentation.navigation.authNavGraph
import com.flysolo.e_appoint.ui.theme.EAPPointTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EAPPointTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                AppointmentApp(windowSize)
            }
        }
    }
}


@Composable
fun AppointmentApp(
    windowSizeClass: WindowSizeClass
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRouter.AUTH.route) {
        authNavGraph(navController)
        composable(
            route = AppRouter.MAIN.route
        ) {
            val viewModel = hiltViewModel<MainViewModel>()
            MainScreen(
                state = viewModel.state,
                events = viewModel::events,
                mainController = navController,
            )
        }

    }
}