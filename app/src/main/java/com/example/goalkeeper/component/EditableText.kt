package com.example.goalkeeper.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditableText(
    text: String,
    onTextChange: (String) -> Unit,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var currentText by remember { mutableStateOf(text) }

    if (isEditing) {
        TextField(
            value = currentText,
            onValueChange = {
                currentText = it
                onTextChange(it)
//                when(flag){
//                    0->todo.name = it
//                    1->todo.memo = it
//                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done // 엔터 키를 체크 표시로 변경
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditing = false
                }
            ),
            textStyle = style,
            modifier = modifier,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White), // 배경색을 흰색으로 설정
        )
    } else {
        Text(
            text = currentText,
            style = style,
            modifier = modifier.clickable { isEditing = true }
        )
    }
}