package com.example.goalkeeper.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val formatterWithTime: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
val formatterWithoutTime: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
fun LocalDateTime.toStringFormat(time:Boolean): String {
    return if (!time) {
        this.format(formatterWithoutTime)
    } else {
        this.format(formatterWithTime)
    }
}

fun String.toLocalDateTime(): LocalDateTime {
    if (this.contains(":")) {
        return LocalDateTime.parse(this, formatterWithTime)
    } else {
        return LocalDateTime.parse(this, formatterWithoutTime)
    }
}
