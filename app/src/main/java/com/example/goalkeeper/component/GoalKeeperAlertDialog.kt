package com.example.goalkeeper.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GoalKeeperAlertDialog(
    title: String,
    text: String,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        dismissButton = dismissButton,
        confirmButton = confirmButton
    )
}