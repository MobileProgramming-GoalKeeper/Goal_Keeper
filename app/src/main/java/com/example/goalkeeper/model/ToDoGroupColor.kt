package com.example.goalkeeper.model

import androidx.compose.ui.graphics.Color

enum class ToDoGroupColor(val rgb: Int) {
    RED(0xE88481),
    ORANGE(0xFEC3A1),
    YELLOW(0xFFD38F),
    GREEN(0xBAD09F),
    BLUE(0xB1C9EF),
    PURPLE(0xC9B6E7),
    PINK(0xFFD1D1),
    BROWN(0x927362);

    override fun toString(): String {
        return name
    }
    fun toColor(): Color {
        return Color(
            red = ((rgb shr 16) and 0xFF) / 255f,
            green = ((rgb shr 8) and 0xFF) / 255f,
            blue = (rgb and 0xFF) / 255f
        )
    }
}
