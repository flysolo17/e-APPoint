package com.flysolo.e_appoint.presentation.main.dashboard

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.presentation.main.appointments.AppointmentAdminCard
import com.flysolo.e_appoint.utils.getColor
import com.flysolo.e_appoint.utils.toBarData
import com.flysolo.e_appoint.utils.toLineChartByStatusAndMonths

import com.flysolo.e_appoint.utils.toPieData
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.rememberPagerState as rememberPagerState1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    state: DashboardState,
    events: (DashboardEvents) -> Unit,
    navHostController: NavHostController
) {
    var data by remember {
        mutableStateOf(state.appointments.toPieData())
    }
    val pagerState = rememberPagerState1(initialPage = 0) { 2  }
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(
            span = { GridItemSpan(1) }
        ) {
            AppointmentData(
                label = "Pending",
                data = state.appointments.filter { it.status == AppointmentStatus.PENDING }.size.toString()
            )
        }

        item(
            span = { GridItemSpan(1) }
        ) {
            AppointmentData(
                label = "Confirmed",
                data = state.appointments.filter { it.status == AppointmentStatus.CONFIRMED }.size.toString()
            )
        }

        item(
            span = { GridItemSpan(1) }
        ) {
            AppointmentData(
                label = "Completed",
                data = state.appointments.filter { it.status == AppointmentStatus.DONE }.size.toString()
            )
        }

        item(
            span = { GridItemSpan(1) }
        ) {
            AppointmentData(
                label = "Cancelled",
                data = state.appointments.filter { it.status == AppointmentStatus.CANCELLED }.size.toString()
            )
        }

        if (state.appointments.isNotEmpty()) {
            item(
                span = { GridItemSpan(2) }
            ){
                val data = state.appointments.toBarData()

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ColumnChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        data = remember { data }, // Use the actual data generated from toBarData()
                        barProperties = BarProperties(
                            spacing = 3.dp,
                            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                            thickness = 10.dp
                        ),
                        gridProperties = GridProperties(
                            enabled = false
                        ),
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),

                    )
                }
            }
        } else {
            item(
                span = { GridItemSpan(2) }
            ) {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Not enough data")
                }
            }
        }

        item(
            span = { GridItemSpan(2) }
        ) {
            val rows = listOf("Pending", "Upcoming")
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(), // You can adjust the width if needed
            ) {
                rows.forEachIndexed { index, tabLabel ->
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
        item(
            span = { GridItemSpan(2) }
        ) {
            HorizontalPager(pagerState) { index ->
                when(index) {
                    0 -> Column {
                        state.appointments.filter {
                            it.status == AppointmentStatus.PENDING
                        }.forEach { data ->
                            AppointmentAdminCard(appointments = data, onDecline = {
                                events(DashboardEvents.OnDeclineAppointment(it))
                            }, onComplete = {
                                events(DashboardEvents.OnCompleteAppointment(it))
                            }, onConfirmed = {
                                events(DashboardEvents.OnConfirmAppointment(it))
                            }, onClick = {

                            })
                        }
                    }
                    1-> Column {
                        state.appointments.filter {
                            it.status == AppointmentStatus.CONFIRMED
                        }.forEach { data ->
                            AppointmentAdminCard(appointments = data, onDecline = {
                                events(DashboardEvents.OnDeclineAppointment(it))
                            }, onComplete = {
                                events(DashboardEvents.OnCompleteAppointment(it))
                            }, onConfirmed = {
                                events(DashboardEvents.OnConfirmAppointment(it))
                            }, onClick = {

                            })
                        }
                    }
                }
            }
        }
    }
}