package com.example.goalkeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.component.ColorPickerDialog
import com.example.goalkeeper.style.AppStyles.korTitleStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun ThemeColorScreen(navController: NavHostController) {

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val user = viewModel.user.value!!

    var colorPickerDialog1Visible by remember { mutableStateOf(false) }
    var colorPickerDialog2Visible by remember { mutableStateOf(false) }

    if (colorPickerDialog1Visible) {
        ColorPickerDialog(
            initialColor = Color(user.themeColor1),
            onDismissRequest = {
                colorPickerDialog1Visible = false
            }) {
            user.themeColor1 = it.toArgb()
            viewModel.updateThemeColor1()
            colorPickerDialog1Visible = false
        }
    } else if (colorPickerDialog2Visible) {
        ColorPickerDialog(
            initialColor = Color(user.themeColor2),
            onDismissRequest = {
                colorPickerDialog2Visible = false
            }) {
            user.themeColor2 = it.toArgb()
            viewModel.updateThemeColor2()
            colorPickerDialog2Visible = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .background(
                            color = Color(user.themeColor1),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { colorPickerDialog1Visible = true },
                )

                Text(text = "테마색 1", textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.padding(start = 20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .background(
                            color = Color(user.themeColor2),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { colorPickerDialog2Visible = true },
                )

                Text(text = "테마색 2", textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.padding(top = 10.dp))
        GoalKeeperButton(
            width = 100,
            height = 100,
            text = "돌아가기",
            textStyle = korTitleStyle
        ) {
            navController.popBackStack()
        }
    }
}