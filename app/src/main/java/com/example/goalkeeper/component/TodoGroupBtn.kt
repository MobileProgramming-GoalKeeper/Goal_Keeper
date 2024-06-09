package com.example.goalkeeper.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goalkeeper.R
import com.example.goalkeeper.model.ToDoGroupColor
import com.example.goalkeeper.model.ToDoGroupIcon
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.style.AppStyles.GroupNameStyle
import com.example.goalkeeper.style.AppStyles.TodoMenuDateStyle
import com.example.goalkeeper.style.AppStyles.TodoMenuTitleStyle

@Composable
fun TodoGroupBtn(toDoGroup: TodoGroup, onClick: ()->Unit) {
    val colorEnum = ToDoGroupColor.valueOf(toDoGroup.color)
    val iconEnum = ToDoGroupIcon.valueOf(toDoGroup.icon)

    Column(modifier=Modifier.clickable{ onClick() },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(colorEnum.toColor())
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconEnum.imgVector,
                contentDescription = toDoGroup.groupName,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 5.dp))
        Text(text=toDoGroup.groupName, style = TextStyle(
            fontFamily = FontFamily(Font(R.font.freesentation_regular)),
            fontSize = 12.sp,
            color = Color.Gray))
    }
}