package com.example.goalkeeper.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.goalkeeper.R
import com.example.goalkeeper.Style.loginTextStyle

@Composable
fun GoalKeeperButton(width: Int, height: Int, text: String, onClickLabel : () -> Unit){
    Box(
        modifier = Modifier.clickable { onClickLabel() },
        contentAlignment = Alignment.Center
    ) {
        RectWithRoundedEnds(width, height, color = colorResource(id = R.color.violet))
        androidx.compose.material3.Text(text = text, style = loginTextStyle)
    }
}