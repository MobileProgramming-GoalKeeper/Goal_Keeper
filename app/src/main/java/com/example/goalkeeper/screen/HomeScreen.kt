package com.example.goalkeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun HomeScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = "Home Screen")
//    }
    Column {
        //Calender()
        //할일 리스트
    }
}


@Composable
fun Calender(modifier: Modifier = Modifier,
             currentDate: LocalDate = LocalDate.now(),
             //config: HorizontalCalendarConfig = HorizontalCalendarConfig(),
             onSelectedDate: (LocalDate) -> Unit) {


}