package com.flysolo.e_appoint.presentation.main.notifications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.utils.BackButton
import com.flysolo.e_appoint.utils.display

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    state: NotificationState,
    events: (NotificationEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(
        state.users
    ) {
        state.users?.id?.let {
            events.invoke(NotificationEvents.OnGetNotifications(it))
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
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(it)
        ) {
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            items(state.inboxes) {
                ListItem(
                    headlineContent = {
                        Text("${it.message}")
                    },
                    supportingContent = {
                        Text(it.createdAt.display())
                    }
                )
            }
        }
    }
}