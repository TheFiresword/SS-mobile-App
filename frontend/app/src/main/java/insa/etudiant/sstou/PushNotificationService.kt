package insa.etudiant.sstou

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import android.app.PendingIntent
import android.content.Intent


object NotificationID {
    private val c = AtomicInteger(0)
    val iD: Int
        get() = c.incrementAndGet()
}


class PushNotificationService : FirebaseMessagingService()  {
    override fun onNewToken(s : String) {
            super.onNewToken(s)
            println("token" + s)
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body
        val CHANNEL_ID = "HEADS_UP_NOTIFICATIONS"
        val channel = NotificationChannel(CHANNEL_ID, "MyNotification", NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        val notification:Notification.Builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title).setContentText(body).setAutoCancel(true).setSmallIcon(R.drawable._logo).setPriority(Notification.PRIORITY_HIGH)

        if (message.data.containsKey("exp")) {
            val exp:String = message.data["exp"]!!
            println("par ici")
            val acceptIntent = Intent(this, NotificationButtonReceiver::class.java).apply{
                putExtra("exp", exp)
            }
            acceptIntent.action = "MY_NOTIFICATION_BUTTON_CLICK"
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification.addAction(R.drawable._accept, getString(R.string.accept),pendingIntent)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NotificationID.iD, notification.build())
        }
    }


    companion object {
//        fun getFCMToken(): String {
//            var token = "blabla"
//            var list = mutableListOf<String>()
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        token = task.result.toString()
//                        println("token = " + token)
//                        return token
//                    } else {
//                        println("token error")
//                    }
//                }
//            return "error"
//        }

    }
}

