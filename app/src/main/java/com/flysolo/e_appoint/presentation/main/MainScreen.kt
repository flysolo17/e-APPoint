package com.flysolo.e_appoint.presentation.main

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.presentation.navigation.BottomNavigationItems
import com.flysolo.e_appoint.presentation.navigation.MainNavGraph
import com.flysolo.e_appoint.utils.BackButton
import com.flysolo.e_appoint.utils.ErrorScreen
import kotlinx.coroutines.launch
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.flysolo.e_appoint.models.users.UserType
import com.flysolo.e_appoint.utils.Avatar
import com.flysolo.e_appoint.utils.shortToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

// NavDrawerItems Data Class
data class NavDrawerItems(
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    events: (MainEvents) -> Unit,
    mainController: NavHostController,
    navHostController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // Define your navigation drawer items
    val drawerItems = listOf(
        NavDrawerItems(
            label = "Personal Info",
            selectedIcon = R.drawable.ic_profile_filled,
            unselectedIcon = R.drawable.ic_person_outline,
            route = AppRouter.PERSONAL_INFO.route
        ),
        NavDrawerItems(
            label = "Contacts",
            selectedIcon = R.drawable.lock_selected,
            unselectedIcon = R.drawable.lock_unselected,
            route = AppRouter.CONTACT_AND_SECURITY.route
        ),
        NavDrawerItems(
            label = "Help Center",
            selectedIcon = R.drawable.help_selected,
            unselectedIcon = R.drawable.help_unselected,
            route = AppRouter.HELP_CENTER.route
        ),
        NavDrawerItems(
            label = "About Us",
            selectedIcon = R.drawable.info_selected,
            unselectedIcon = R.drawable.info_unselected,
            route = AppRouter.ABOUT_US.route
        )
    )

    var selectedRoute by remember { mutableStateOf(drawerItems.first().route) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer Header
                ListItem(
                    headlineContent = { Text("${state.users?.name}") },
                    supportingContent = { Text("${state.users?.phone}") },
                    leadingContent = {
                        Avatar(
                            url = "${state.users?.profile}",
                            size = 40.dp
                        ) { }
                    }
                )

                HorizontalDivider()
                Spacer(
                    modifier = modifier.height(16.dp)
                )
                // Drawer Items
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = selectedRoute == item.route,
                        onClick = {
                            selectedRoute = item.route
                            scope.launch { drawerState.close() }
                            navHostController.navigate(item.route) {
                                popUpTo(navHostController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (selectedRoute == item.route) item.selectedIcon else item.unselectedIcon
                                ),
                                contentDescription = "${item.label} Icon"
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                // Drawer Footer
                Button(
                    onClick = {
                        events(MainEvents.Logout)
                        scope.launch {
                            context.shortToast("Successfully Logget out!")
                            delay(1000)
                            mainController.navigate(AppRouter.AUTH.route)
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Logout")
                }
            }
        }
    ) {
        val items = BottomNavigationItems.BOTTOM_NAV
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val isBottomNavItems = items.any { it.route == currentRoute }
        val isDrawerItems = drawerItems.any { it.route == currentRoute }
        if (isBottomNavItems || isDrawerItems) {
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
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu Icon"
                                )
                            }
                        },
                        title = {
                            val route = currentRoute?.toString()
                                ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                            Text(route ?:"e-APPoint")
                        },
                        actions = {
                            if (state.users?.type == UserType.CLIENT) {
                                IconButton(
                                    onClick = {navHostController.navigate(AppRouter.NOTIFICATIONS.route)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications"
                                    )
                                }
                                IconButton(
                                    onClick = {navHostController.navigate(AppRouter.CREATE_APPOINTMENT.route)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Create"
                                    )
                                }
                            }

                        }
                    )
                },
                bottomBar = {
                    if (currentRoute != AppRouter.PROFILE.route && !isDrawerItems) {
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
                                onClick = { navHostController.popBackStack() }
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
}
