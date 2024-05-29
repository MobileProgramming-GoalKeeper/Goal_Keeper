package com.example.goalkeeper.Style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.goalkeeper.R

val EngTitleStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.wedges)),
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    color = Color.White
)
val KorTitleStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.freesentation_8extrabold)),
    fontWeight = FontWeight.Bold,
    fontSize=30.sp,
    color = Color.White
)

val loginTextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.freesentation_6semibold)),
    fontWeight = FontWeight.Bold,
    fontSize=20.sp,
    color = Color.White
)