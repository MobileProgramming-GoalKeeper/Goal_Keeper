package com.example.goalkeeper.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//반원모서리 사각형
@Composable
fun RectWithRoundedEnds(width: Int, height: Int, color: Color = Color.LightGray) {
    Box(
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .background(
                color = color,
                shape = RoundedCornerShape((height / 2).dp) // 높이의 절반에 해당하는 반경으로 모서리를 둥글게 처리
            )
    )
}