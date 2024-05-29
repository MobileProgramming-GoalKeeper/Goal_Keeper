package com.example.goalkeeper.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.goalkeeper.R
import com.example.goalkeeper.Style.loginTextStyle
import com.example.goalkeeper.component.RectWithRoundedEnds

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "로고",
            modifier = Modifier.size(180.dp)
        )
        RectWithRoundedEnds(300, 60)
        Spacer(modifier = Modifier.padding(5.dp))
        RectWithRoundedEnds(300, 60)
        Spacer(modifier = Modifier.padding(20.dp))
        Box(modifier = Modifier.clickable { navController.navigate("main")},
            contentAlignment = Alignment.Center
        ) {
            RectWithRoundedEnds(230, 60, color = colorResource(id = R.color.violet))
            androidx.compose.material3.Text(text = "로그인/시작하기", style = loginTextStyle)
        }
    }
}

