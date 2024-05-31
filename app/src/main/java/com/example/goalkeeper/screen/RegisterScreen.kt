package com.example.goalkeeper.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.goalkeeper.R
import com.example.goalkeeper.Style.loginTextStyle
import com.example.goalkeeper.component.RectWithRoundedEnds
import com.example.goalkeeper.component.GoalKeeperTextField

@Composable
fun RegisterScreen(navController: NavHostController) {

    var userID by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "회원가입",
            fontFamily = FontFamily(Font(R.font.freesentation_8extrabold)),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = colorResource(R.color.violet),
            modifier = Modifier.padding(20.dp)
        )

        GoalKeeperTextField(width = 300, height = 60, value = userID, label = "ID") { userID = it }
        Spacer(modifier = Modifier.padding(5.dp))
        GoalKeeperTextField(
            width = 300,
            height = 60,
            value = userPassword,
            label = "PASSWORD"
        ) { userPassword = it }
        Spacer(modifier = Modifier.padding(20.dp))

        Box(
            modifier = Modifier.clickable {
                /* 회원 정보 DB에 저장 기능 추가 */

                navController.navigate("welcome")
            },
            contentAlignment = Alignment.Center
        ) {
            RectWithRoundedEnds(230, 60, color = colorResource(id = R.color.violet))
            androidx.compose.material3.Text(text = "완료", style = loginTextStyle)
        }
    }
}

