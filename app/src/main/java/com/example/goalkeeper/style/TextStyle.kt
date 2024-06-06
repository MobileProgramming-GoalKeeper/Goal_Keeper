package com.example.goalkeeper.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.goalkeeper.R

object AppStyles {
    val engTitleStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.wedges)),
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = Color.White
    )

    val korTitleStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_8extrabold)),
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = Color.White
    )

    val loginTextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_6semibold)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White
    )
    val GroupNameStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_regular)),
        fontSize = 20.sp,
        color = Color.Black,
        fontWeight = FontWeight.ExtraBold
    )
    val TodoNameStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_regular)),
        fontSize = 20.sp,
        color = Color.Black
    )
    val TodoMemoStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_regular)),
        fontSize = 15.sp,
        color = Color.DarkGray
    )
    val TodoMenuTitleStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_8extrabold)),
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        color = Color.Black
    )
    val TodoMenuDateStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.freesentation_regular)),
        fontSize = 15.sp,
        color = Color.Gray
    )
}
