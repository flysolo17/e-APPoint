package com.flysolo.e_appoint.presentation.drawer.personal_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flysolo.e_appoint.presentation.drawer.about.RowData


@Composable
fun PersonalInfoScreen(
    modifier: Modifier = Modifier,
    state: InfoState,
    events: (InfoEvents ) -> Unit,
    navHostController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize().background(
            color = MaterialTheme.colorScheme.primary
        ).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Text("Profile Info", style = MaterialTheme.typography.titleLarge)
                RowData(
                    label = "Name:",
                    value = "${state.users?.name}"
                )
                RowData(
                    label = "Email:",
                    value = "${state.users?.email}"
                )
                RowData(
                    label = "Phone:",
                    value = "${state.users?.phone}"
                )
                RowData(
                    label = "User TYpe:",
                    value = "${state.users?.type}"
                )
            }
        }
    }
}
