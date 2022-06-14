package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.utils.Arguments.RANDOM_INT
import kotlin.random.Random

class FcmService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
//        Log.d("onMessageReceived", gson.toJson(message))
        message.data[action]?.let {
            when (it) {
                "LIKE" -> handleNotification(
                    gson.fromJson(
                        message.data[content],
                        Notification::class.java
                    ), R.string.notification_user_liked
                )
                "SHARE" -> handleNotification(
                    gson.fromJson(
                        message.data[content],
                        Notification::class.java
                    ), R.string.notification_user_share
                )
                "VIEW" -> handleNotification(
                    gson.fromJson(
                        message.data[content],
                        Notification::class.java
                    ), R.string.notification_new_viewer
                )
                "NEW_POST" -> handleNotification(
                    gson.fromJson(
                        message.data[content],
                        Notification::class.java
                    ), R.string.notification_new_post
                )
                else -> handleNotification(
                    gson.fromJson(
                        message.data[content],
                        Notification::class.java
                    ), R.string.notification_else_action
                )
            }
        }
    }

    override fun onNewToken(token: String) {
//        Log.d("onNewToken", token)
        println("$token")
    }

    private fun handleNotification(content: Notification, text: Int) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.netology)
            .setContentTitle(
                getString(
                    text,
                    content.userName,
                    content.postId
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(RANDOM_INT), notification)
    }
}

data class Notification(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
    val text: String
)