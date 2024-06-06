package com.example.goalkeeper.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")

fun LocalDateTime.toStringFormat(): String {
    return this.format(formatter)
}

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, formatter)
}