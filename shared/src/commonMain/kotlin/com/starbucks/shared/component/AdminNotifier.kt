package com.starbucks.shared.component

import com.mmk.kmpnotifier.notification.NotifierManager
import kotlin.random.Random

suspend fun sendAdminLocalNotification(title: String, body: String) {
    val notifier = NotifierManager.getLocalNotifier()
    notifier.notify {
        id = Random.nextInt()
        this.title = title
        this.body = body
        payloadData = mapOf("screen" to "AdminPanel")
    }
}