package com.flysolo.e_appoint.presentation.drawer.about

import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
                Text("About the Clinic", style = MaterialTheme.typography.titleLarge)
                RowData(
                    label = "Address:",
                    value = "First Floor E.Q. Building, OMSC San Jose\n" +
                            "Campus, Quirino Street, San Jose, Occidental\n" +
                            "Mindoro\n"
                )
                RowData(
                    label = "Clinic Hours: ",
                    value = "Monday to Friday, 8:00 AM to 5:00 PM"
                )
                RowData(
                    label = "Services:",
                    value = "- Dental Check-up\n" +
                            "- Oral Prophylaxis (Dental Cleaning)\n" +
                            "- Tooth Extraction\n" +
                            "- Temporary Restoration\n" +
                            "- Medication"
                )
                Text("A healthy smile, a happy you!")
                Text("We’re so glad you chose our clinic for your dental care. Your oral health is important\n" +
                        "to us, and we’re committed to providing you with the best possible care.\n" +
                        "Remember, a healthy smile can boost your confidence and overall well-being. Let’s\n" +
                        "keep that smile shining bright!",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun RowData(
    modifier: Modifier = Modifier,
    label : String,
    value : String,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = modifier.weight(0.4f),
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color.Gray,
                textAlign = TextAlign.Start
            )
        )
        Text(
            modifier = modifier.weight(1f),
            text = value,

            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        )
    }
}