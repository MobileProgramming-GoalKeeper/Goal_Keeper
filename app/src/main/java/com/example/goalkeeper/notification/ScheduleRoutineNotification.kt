package com.example.goalkeeper.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.goalkeeper.model.NotificationType
import com.example.goalkeeper.model.UserRoutine
import java.util.Calendar

fun scheduleRoutineNotification(context: Context, routine: UserRoutine) {
    if (routine.routineAlert) {
        Log.d("RoutineNotification", "Routine notification scheduled for ${routine.routineName}")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, routine.hour)
            set(Calendar.MINUTE, routine.minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        setRoutineNotification(context, routine, calendar.timeInMillis)
    } else {
        cancelRoutineNotification(context, routine)
    }
}


@SuppressLint("ScheduleExactAlarm")
fun setRoutineNotification(context: Context, routine: UserRoutine, triggerTime: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, RoutineBroadcastReceiver::class.java).apply {
        putExtra("routineName", routine.routineName)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, routine.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    when (routine.notificationType) {
        NotificationType.DAILY -> {
            // 매일 반복 알람 설정
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        NotificationType.WEEKDAY -> {
            // 평일 반복 알람 설정 (월요일부터 금요일까지)
            setWeekdayAlarm(alarmManager, triggerTime, pendingIntent)
        }
        NotificationType.WEEKEND -> {
            // 주말 반복 알람 설정 (토요일과 일요일)
            setWeekendAlarm(alarmManager, triggerTime, pendingIntent)
        }
    }
}

private fun setWeekdayAlarm(alarmManager: AlarmManager, triggerTime: Long, pendingIntent: PendingIntent) {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = triggerTime
    }

    for (day in Calendar.MONDAY..Calendar.FRIDAY) {
        calendar.set(Calendar.DAY_OF_WEEK, day)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }
}

private fun setWeekendAlarm(alarmManager: AlarmManager, triggerTime: Long, pendingIntent: PendingIntent) {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = triggerTime
    }

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY * 7,
        pendingIntent
    )

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY * 7,
        pendingIntent
    )
}

fun cancelRoutineNotification(context: Context, routine: UserRoutine) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, RoutineBroadcastReceiver::class.java).apply {
        putExtra("routineName", routine.routineName)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, routine.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(pendingIntent)
}
