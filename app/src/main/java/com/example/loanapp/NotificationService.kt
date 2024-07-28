package com.example.loanapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, "pin")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}