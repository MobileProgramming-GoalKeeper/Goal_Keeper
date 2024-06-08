package com.example.goalkeeper.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.goalkeeper.R

@Composable
fun GoalKeeperAlertDialog(title: String, text: String, onDismissRequest: () -> Unit, onClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.violet),
                    contentColor = colorResource(id = R.color.white)
                ),
                onClick = onClick
            ) {
                Text("확인")
            }
        }
    )
}