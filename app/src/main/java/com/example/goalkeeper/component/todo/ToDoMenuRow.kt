package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.goalkeeper.style.AppStyles


@Composable
fun TodoMenuRow(title: String, icon: Int, onClick: () -> Unit) {
    Spacer(modifier = Modifier.padding(10.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.DarkGray)
                .size(50.dp),
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                painter = painterResource(id = icon), contentDescription = title,
                tint = Color.White
            )
        }
        Text(
            title, style = AppStyles.TodoNameStyle,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}