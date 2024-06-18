package com.example.goalkeeper.model

enum class NotificationType {
    DAILY,
    WEEKDAY,
    WEEKEND
}
data class UserRoutine(
    var routineName: String,
    var routineAlert: Boolean,
    var hour: Int,
    var minute: Int,
    var notificationType: NotificationType
)
{
    constructor():this("noName", false, 8, 0,NotificationType.DAILY)
}
