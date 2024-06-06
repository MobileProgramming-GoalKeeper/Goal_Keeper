package com.example.goalkeeper.model

import androidx.compose.ui.graphics.Color

enum class ToDoGroupColor(val rgb: Int) {
    RED(0xFF0000),
    ORANGE(0xFFA500),
    YELLOW(0xFFFF00),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    PURPLE(0x800080),
    PINK(0xFFC0CB),
    BROWN(0xA52A2A);

    fun toColor(): Color {
        return Color(
            red = ((rgb shr 16) and 0xFF) / 255f,
            green = ((rgb shr 8) and 0xFF) / 255f,
            blue = (rgb and 0xFF) / 255f
        )
    }
}
