package com.flysolo.e_appoint.presentation.main.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.utils.Avatar


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    events: (HomeEvents) -> Unit,
    navHostController: NavHostController
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ProfileInfo(users = state.users)
        }
        item {
            ClinicInfo(onCall = { phone ->
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
            })
        }
        item {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Find Your Dental Solution",
                    style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Start),
                    modifier = modifier.weight(1f)
                )
                Spacer(modifier = modifier.width(16.dp))
                Button(
                    onClick = {
                        navHostController.navigate(AppRouter.CREATE_APPOINTMENT.route)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                        Text("Create")
                    }
                }
            }

        }
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = modifier.weight(1f),
                    onClick = {
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContactMail,
                            contentDescription = "Contact"
                        )
                        Text("Contact clinic")
                    }
                }
                Button(
                    modifier = modifier.weight(1f),
                    onClick = {
                        navHostController.navigate(AppRouter.NOTIFICATIONS.route)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                        Text("Notifications")
                    }
                }
            }
        }
    }
}


@Composable
fun ClinicInfo(
    modifier: Modifier = Modifier,
    onCall : (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ){
        Row(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text("OMSC-SJ Dental Unit", style = MaterialTheme.typography.titleLarge)
                Text("Address : First Floor E.Q. Building, OMSC San Jose Campus, Quirino Street, San Jose, Occidental Mindoro", style = MaterialTheme.typography.labelSmall)
                Text("Clinic Hours: Monday to Friday, 8:00 AM to 5:00 PM", style = MaterialTheme.typography.labelSmall)
                Text("Contact : 09998949746", style = MaterialTheme.typography.labelSmall)
            }
            IconButton(
                onClick = {onCall("09998949746")}
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call"
                )
            }
        }
    }
}
@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    users: Users?
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        leadingContent = {
            Avatar(url = users?.profile?:"", size = 60.dp) { }
        },
        headlineContent = { Text("${users?.name}") },
        supportingContent = {
            Text("${users?.email}")
        }
    )
}
