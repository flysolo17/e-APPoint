package com.flysolo.e_appoint.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CancelableIconButton(
    onCancel: (String) -> Unit,
    appointmentId: String?,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Cancel,
    confirmationMessage: String = "Are you sure you want to cancel this appointment?"
) {
    var showDialog by remember { mutableStateOf(false) }

    // Confirmation dialog state
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Cancellation") },
            text = { Text(confirmationMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        appointmentId?.let {
                            onCancel(it)
                        }
                        showDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    // The IconButton that triggers the dialog
    FilledIconButton(
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Cancel"
        )
    }
}
