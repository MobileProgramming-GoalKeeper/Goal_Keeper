package com.example.goalkeeper.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.example.goalkeeper.R

@Composable
fun GoalKeeperButton(width: Int, height: Int, text: String, textStyle: TextStyle, onClickLabel : () -> Unit){
    Box(
        modifier = Modifier.clickable { onClickLabel() },
        contentAlignment = Alignment.Center
    ) {
        RectWithRoundedEnds(width, height, color = colorResource(id = R.color.violet))
        Text(text = text, style = textStyle)
    }
}