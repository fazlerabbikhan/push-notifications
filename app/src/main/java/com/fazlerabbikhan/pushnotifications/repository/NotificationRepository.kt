package com.fazlerabbikhan.pushnotifications.repository

import com.fazlerabbikhan.pushnotifications.data.Notification

object NotificationRepository {
    private val notifications = mutableListOf<Notification>()

    fun addNotification(notification: Notification) {
        notifications.add(notification)
    }

    fun getNotifications(): List<Notification> {
        return notifications
    }
}