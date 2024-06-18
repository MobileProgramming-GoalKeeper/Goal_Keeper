package com.example.goalkeeper.component.todo.timealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.goalkeeper.model.Todo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("ScheduleExactAlarm", "SuspiciousIndentation")
fun setTodoAlarm(context: Context, todo: Todo) {
    // 알람 매니저 선언
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    // 데이터 담기
    intent.putExtra("todoName", todo.todoName)
    intent.putExtra("notificationId", todo.todoId.hashCode())

    val pendingIntent = PendingIntent.getBroadcast(context, todo.todoId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    try{
        if (todo.todoAlert) {
            val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())
            val dateTime = "${todo.todoStartAt}"
            val alarmTime = formatter.parse(dateTime)?.time ?: throw IllegalArgumentException("Invalid date time format")

            val test = System.currentTimeMillis()
                if (alarmTime > System.currentTimeMillis()) { // 알람 설정된 시간 > 현재 시간
                    //createTodoAlarmChannel(context, "todoAlarm_Channel") // 알림 채널 생성
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, (alarmTime-test), pendingIntent)

                    Toast.makeText(context, "에 알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "유효하지 않은 시간입니다.", Toast.LENGTH_SHORT).show()
            }

        } else {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(context, "알림이 해제되었습니다.", Toast.LENGTH_SHORT).show()
        }
        }catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "알림 설정 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

}


