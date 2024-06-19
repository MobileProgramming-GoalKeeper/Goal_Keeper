package com.example.goalkeeper.component.todo.timealarm

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.goalkeeper.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("notificationId", 0)
        val todoName = intent.getStringExtra("todoName")

        val builder = NotificationCompat.Builder(context, "todoAlarm_Channel")
            .setSmallIcon(R.drawable.baseline_event_repeat_24)
            .setContentTitle("지금 할 일이 있습니다")
            .setContentText(todoName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(alarmId, builder.build())
        }
    }

}
//
//    private fun createNotification(context: Context, todoName: String) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationId = System.currentTimeMillis().toInt()
//        val notificationIntent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val notificationBuilder = NotificationCompat.Builder(context, "todo_channel_id")
//            .setSmallIcon(R.drawable.ic_notification) // 알림 아이콘 리소스 확인
//            .setContentTitle("Todo 알림")
//            .setContentText(todoName)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "todo_channel_id"
//            val channelName = "Todo 알림"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(channelId, channelName, importance)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(notificationId, notificationBuilder.build())
//    }

//
//class NotificationHelper(base: Context?) : ContextWrapper(base) {
//
//    private val channelID = "channelID"
//    private val channelNm = "channelNm"
//
//    init {
//        //안드로이드 버전이 오레오거나 이상이면 채널 생성
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//            //채널 생성
//            createChannel()
//        }
//    }
//
//    //채널 생성
//    private fun createChannel(){
//        var channel = NotificationChannel(channelID, channelNm,
//            NotificationManager.IMPORTANCE_DEFAULT)
//
//        channel.enableLights(true)
//        channel.enableVibration(true)
//        channel.lightColor = Color.GREEN
//        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//
//        getManager().createNotificationChannel(channel)
//    }
//
//    //NotificationManager 생성
//    fun getManager(): NotificationManager{
//
//        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//    }
//
//    //notification 설정
//    fun getChannelNotification(name: String?):NotificationCompat.Builder{
//
//        return NotificationCompat.Builder(applicationContext, channelID)
//            .setContentTitle("지금 할 일이 있습니다")
//            .setContentText(name)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//    }
//}
//
