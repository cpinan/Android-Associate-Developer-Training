package com.carlospinan.android.associate.dev

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_notify_me.*

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val NOTIFICATION_ID = 7777

class NotifyMeActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_me)

        createNotificationChannel()

        notify.setOnClickListener {
            sendNotification()
        }

        update.setOnClickListener {
            updateNotification()
        }

        cancel.setOnClickListener {
            cancelNotification()
        }
        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnabled = false, isCancelEnabled = false)
    }

    private fun createNotificationChannel() {
        notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification Channel for Mascot"

            notificationManager.createNotificationChannel(
                notificationChannel
            )

        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(
            this,
            PRIMARY_CHANNEL_ID
        )
            .setContentTitle("CONTENT TITLE")
            .setContentText("CONTENT TEXT")
            .setSmallIcon(android.R.drawable.ic_menu_add)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
    }

    private fun sendNotification() {
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnabled = true, isCancelEnabled = true)
        notificationManager.notify(
            NOTIFICATION_ID,
            getNotificationBuilder().build()
        )
    }

    private fun updateNotification() {
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnabled = false, isCancelEnabled = true)
        val resource = BitmapFactory.decodeResource(
            resources,
            R.drawable.mascot_1
        )
        val builder = getNotificationBuilder()
        builder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(resource)
                .setBigContentTitle("Notification Updated!")
        )
        notificationManager.notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    private fun cancelNotification() {
        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnabled = false, isCancelEnabled = false)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun setNotificationButtonState(
        isNotifyEnabled: Boolean,
        isUpdateEnabled: Boolean,
        isCancelEnabled: Boolean
    ) {
        notify.isEnabled = isNotifyEnabled
        update.isEnabled = isUpdateEnabled
        cancel.isEnabled = isCancelEnabled
    }

}