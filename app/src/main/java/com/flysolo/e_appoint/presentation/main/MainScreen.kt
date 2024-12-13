package com.flysolo.e_appoint.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.navigation.BottomNavigationItems
import com.flysolo.e_appoint.presentation.navigation.MainNavGraph
import com.flysolo.e_appoint.utils.BackButton
import com.flysolo.e_appoint.utils.ErrorScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    events: (MainEvents) -> Unit,
    mainController: NavHostController,
    navHostController: NavHostController = rememberNavController()
) {
    val items = BottomNavigationItems.BOTTOM_NAV
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isBottomNavItems = items.any { it.route == currentRoute }
    if (isBottomNavItems) {
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
                        if (currentRoute == AppRouter.PROFILE.route) {
                            BackButton { navHostController.popBackStack() }
                        }
                    },
                    title = {
                        Text("e-APPoint")
                    },
                )
            },
            bottomBar = {
                if (currentRoute != AppRouter.PROFILE.route) {
                    BottomNavigation(
                        items = items,
                        navBackStackEntry = navBackStackEntry,
                        navHostController = navHostController
                    )
                }
            }
        ) {
            Box(
                modifier = modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.errors != null -> ErrorScreen(message = state.errors) {
                        Button(
                            onClick = {navHostController.popBackStack()}
                        ) { Text("Back") }
                    }
                    else -> {
                        MainNavGraph(
                            navHostController = navHostController,
                            user = state.users,
                            mainNavController = mainController
                        )
                    }
                }
            }
        }
    } else {
        MainNavGraph(
            navHostController = navHostController,
            user = state.users,
            mainNavController = mainController
        )
    }

}