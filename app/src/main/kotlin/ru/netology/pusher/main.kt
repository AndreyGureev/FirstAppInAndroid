package ru.netology.pusher

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream


fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        .build()

    FirebaseApp.initializeApp(options)

    val message = Message.builder()
        .putData("action", "NEW_POST")
        .putData(
            "content", """{
          "postId": 1,
          "postAuthor": "Нетология. Университет интернет-профессий будущего",
          "content": "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb"
          }""".trimIndent()
        )
        .setToken("clzRv0XWR1iU5Hl399Yk1W:APA91bEND5RFl6dmKC-m3w6pEPNe6FHD2OoE1As4cLi6SXd1Iz_bKug_41J29QKTb4R0SosKikqBQapRvzM74gBjENRccMQY9SaXKcPCIPZFVhDv5Ggu_JYdk0mWJdimNAn9Bq2Sy3oZ")
        .build()

    FirebaseMessaging.getInstance().send(message)
}