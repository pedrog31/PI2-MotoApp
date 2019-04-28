package co.edu.udea.motoapp.servicios

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.NuevaRuta
import co.edu.udea.motoapp.actividad.Principal
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FCM Service"
    private val bitmap: Bitmap? = null
    private val context = this

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "Dikirim dari: ${remoteMessage!!.from}")

        if (remoteMessage!!.notification != null) {
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }

    }
    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, Principal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val channelId = "Default"

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.motorcycle_notif)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setColor(0xff3600)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH)
            channel.setDescription("Channel description")
            channel.enableLights(true)
            channel.setLightColor(Color.RED)
            channel.setVibrationPattern(longArrayOf(0, 1000, 500, 1000))
            channel.enableVibration(true)
            channel.setShowBadge(true)
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, notificationBuilder.build())
    }


    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = 0xff3600
            notificationBuilder.setColor(color)
            return R.drawable.motorcycle_notif

        } else {
            return R.drawable.motorcycle_notif
        }
    }

    override fun onNewToken(fcmToken: String?) {
        super.onNewToken(fcmToken)
        Log.d("FCMService", fcmToken)
    }

    fun grabFcmToken(){
        val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion("Error cargando datos para el registro")
        val repositorioMoteros = FirebaseDatabase.getInstance().getReference("moteros")
        var a = FirebaseInstanceId.getInstance().
            instanceId.addOnCompleteListener {task ->
            if (task.isSuccessful){
                Log.d("FCMService", task.result!!.token)
                repositorioMoteros.child(moteroActual.uid).child("fcmToken").setValue(task.result!!.token)
            }
        }
    }
}