package com.example.goalkeeper.screen.MyPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.goalkeeper.R

@Composable
fun HSVColorPicker(
    hue: Float,
    saturation: Float,
    value: Float,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "색상")
        Slider(
            value = hue,
            valueRange = 0f..360f,
            onValueChange = onHueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.violet),
                activeTrackColor = colorResource(id = R.color.violet),
                inactiveTrackColor = colorResource(id = R.color.light_pink)
            )
        )

        Text(text = "채도")
        Slider(
            value = saturation,
            valueRange = 0f..1f,
            onValueChange = onSaturationChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.violet),
                activeTrackColor = colorResource(id = R.color.violet),
                inactiveTrackColor = colorResource(id = R.color.light_pink)
            )
        )

        Text(text = "명도")
        Slider(
            value = value,
            valueRange = 0f..1f,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.violet),
                activeTrackColor = colorResource(id = R.color.violet),
                inactiveTrackColor = colorResource(id = R.color.light_pink)
            )
        )

        val color = hsvToColor(hue, saturation, value)
        Box(
            modifier = Modifier
                .size(100.dp, 100.dp)
                .background(color = color, shape = RoundedCornerShape(50.dp))
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(50.dp))
        )
    }
}