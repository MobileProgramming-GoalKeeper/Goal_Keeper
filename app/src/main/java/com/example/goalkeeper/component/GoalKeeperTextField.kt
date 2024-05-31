package com.example.goalkeeper.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.goalkeeper.Style.loginTextStyle

@Composable
fun GoalKeeperTextField(width: Int, height: Int, color: Color = Color.LightGray, value: String, label: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .background(
                color = color,
                shape = RoundedCornerShape((height / 2).dp)
            )
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxSize(),
            colors =  textFieldColors(
                textColor = Color.White,
                disabledTextColor = Color.White,
                backgroundColor = color,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = CircleShape,
            textStyle = loginTextStyle
        )
    }
}