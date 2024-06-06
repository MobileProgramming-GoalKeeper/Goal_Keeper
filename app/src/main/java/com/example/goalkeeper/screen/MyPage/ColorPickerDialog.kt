package com.example.goalkeeper.screen.MyPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val hsv = colorToHsv(initialColor)

    var hue by remember { mutableStateOf(hsv[0]) }
    var saturation by remember { mutableStateOf(hsv[1]) }
    var value by remember { mutableStateOf(hsv[2]) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "색상 변경")

                Spacer(modifier = Modifier.height(16.dp))

                HSVColorPicker(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onHueChange = { hue = it },
                    onSaturationChange = { saturation = it },
                    onValueChange = { value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("취소")
                    }
                    TextButton(onClick = {
                        val selectedColor = hsvToColor(hue, saturation, value)
                        onColorSelected(selectedColor)
                    }) {
                        Text("변경")
                    }
                }
            }
        }
    }
}

fun colorToHsv(color: Color): List<Float> {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = max(r, max(g, b))
    val min = min(r, min(g, b))
    val delta = max - min
    val v = max

    val s: Float = if (max == 0f) 0f else delta / max

    val h: Float = when {
        max == min -> 0f
        max == r -> (60 * ((g - b) / delta) + 360) % 360
        max == g -> (60 * ((b - r) / delta) + 120) % 360
        else -> (60 * ((r - g) / delta) + 240) % 360
    }

    return listOf(h, s, v)
}

fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val c = value * saturation
    val x = c * (1 - abs((hue / 60) % 2 - 1))
    val m = value - c

    val (r, g, b) = when (hue.toInt()) {
        in 0..59 -> Triple(c, x, 0f)
        in 60..119 -> Triple(x, c, 0f)
        in 120..179 -> Triple(0f, c, x)
        in 180..239 -> Triple(0f, x, c)
        in 240..299 -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color((r + m), (g + m), (b + m))
}