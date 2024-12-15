package com.flysolo.e_appoint.presentation.main.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AppointmentData(
    modifier: Modifier = Modifier,
    label : String,
    data : String
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ){
            Text("${data}", style = MaterialTheme.typography.titleLarge)
            Text("${label}", style = MaterialTheme.typography.labelMedium.copy(
                color = Color.Gray
            ))
        }
    }
}