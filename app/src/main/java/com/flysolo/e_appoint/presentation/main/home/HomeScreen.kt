package com.flysolo.e_appoint.presentation.main.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.config.AppRouter
import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentAdminCard
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentUserCard
import com.flysolo.e_appoint.presentation.main.dashboard.DashboardEvents
import com.flysolo.e_appoint.utils.Avatar
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    events: (HomeEvents) -> Unit,
    navHostController: NavHostController
) {

    LaunchedEffect(state.users) {
        state.users?.id?.let {
            events(HomeEvents.OnGetAppointments(it))
        }
    }
    val tabs = listOf(
        "Pending",
        "Upcoming",
    )
    val pagerState = rememberPagerState(initialPage = 0) { 3  }
    val scope = rememberCoroutineScope()

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
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
            ) {
                tabs.forEachIndexed { index, tabLabel ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick ={
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            val pendingCount = state.appointments.filter { it.status == AppointmentStatus.PENDING }.size
                            val upcomingCount = state.appointments.filter { it.status == AppointmentStatus.CONFIRMED }.size
                            BadgedBox(
                                badge = {
                                    val count = when (tabLabel) {
                                        "Pending" -> pendingCount
                                        "Upcoming" -> upcomingCount
                                        else -> 0
                                    }
                                    if (count > 0) {
                                        Badge { Text(count.toString()) }
                                    }
                                }
                            ) {
                                Text(tabLabel)
                            }
                        }
                    )
                }
            }
        }

        item {
            HorizontalPager(pagerState) { index ->
                when(index) {
                    0 -> Column {
                        state.appointments.filter {
                            it.status == AppointmentStatus.PENDING
                        }.forEach { data ->
                            AppointmentUserCard(
                                appointments = data,
                                onCancel = {
                                    data?.id?.let {
                                        events(HomeEvents.OnCancel(data.id))
                                    }
                                }, onClick = {})
                        }
                    }
                    1-> Column {
                        state.appointments.filter {
                            it.status == AppointmentStatus.CONFIRMED
                        }.forEach { data ->
                            AppointmentUserCard(
                                appointments = data,
                                onCancel = {
                                    data?.id?.let {
                                        events(HomeEvents.OnCancel(data.id))
                                    }
                                }, onClick = {})
                        }
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
        Column(
            modifier = modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text("OMSC-SJ Dental Unit", style = MaterialTheme.typography.titleLarge)
            Text("Address : First Floor E.Q. Building, OMSC San Jose Campus, Quirino Street, San Jose, Occidental Mindoro", style = MaterialTheme.typography.labelSmall)
            Text("Clinic Hours: Monday to Friday, 8:00 AM to 5:00 PM", style = MaterialTheme.typography.labelSmall)
            Text("Contact : 09998949746", style = MaterialTheme.typography.labelSmall)
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
