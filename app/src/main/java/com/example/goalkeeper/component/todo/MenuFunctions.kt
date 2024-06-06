package com.example.goalkeeper.component.todo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.toLocalDateTime
import com.example.goalkeeper.model.toStringFormat
import java.time.LocalDateTime

fun DoItTomorrow(todo: Todo): Todo {
    val newDate = todo.todoDate.toLocalDateTime().plusDays(1)

    val newStartAt = todo.todoStartAt?.toLocalDateTime()?.plusDays(1)?.toStringFormat()
    val newEndAt = todo.todoEndAt?.toLocalDateTime()?.plusDays(1)?.toStringFormat()

    return todo.copy(
        todoDate = newDate.toStringFormat(),
        todoStartAt = newStartAt,
        todoEndAt = newEndAt
    )
}
fun ChangeDate(todo: Todo, newDate: LocalDateTime): Todo {
    val durationBetweenDateAndStart = todo.todoStartAt?.toLocalDateTime()
        ?.let { java.time.Duration.between(todo.todoDate.toLocalDateTime(), it) }
    val durationBetweenDateAndEnd = todo.todoEndAt?.toLocalDateTime()
        ?.let { java.time.Duration.between(todo.todoDate.toLocalDateTime(), it) }

    val newStartAt = durationBetweenDateAndStart?.let { newDate.plus(it) }
    val newEndAt = durationBetweenDateAndEnd?.let { newDate.plus(it) }

    return todo.copy(
        todoDate = newDate.toStringFormat(),
        todoStartAt = newStartAt?.toStringFormat(),
        todoEndAt = newEndAt?.toStringFormat()
    )
}

fun copyToClipboard(context: Context, todo: Todo) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("ToDo", todo.toText())
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
}