package com.blogspot.yourfavoritekaisar.mygithubui.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.ui.home.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_ALARM_REPEATING = "MyGithubUi"
        const val TYPE_ALARM_ONE_TIME = "OnetTImeAlarm"

        const val EXTRA_TYPE = "type"

        private const val ID_ONE_TIME = 100
        private const val ID_REPEATING = 101

        private const val TIME_FORMAT = "HH:mm"
    }

    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val type = intent.getStringExtra(EXTRA_TYPE)

        val title = if (type.equals(
                TYPE_ALARM_ONE_TIME,
                ignoreCase = true
            )
        ) TYPE_ALARM_ONE_TIME else TYPE_ALARM_REPEATING
        val notifId =
            if (type.equals(TYPE_ALARM_ONE_TIME, ignoreCase = true)) ID_ONE_TIME else ID_REPEATING

        showAlarmNotification(context, title, notifId)
        Log.d(TAG, "Alarm on")
    }

    fun setRepeatAlarm(context: Context, type: String, time: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":").toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
            Log.d(TAG, "Set Time Success")
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Reminder on $time AM", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Alarm $time AM On")
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode =
            if (type.equals(TYPE_ALARM_ONE_TIME, ignoreCase = true)) ID_ONE_TIME else ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Reminder off", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Alarm Off")
    }

    private fun showAlarmNotification(context: Context, title: String, notifId: Int) {
        val channelId = "Channel_1"
        val channelName = "Alarmmanager channel"

        val notificationIntent = Intent(context, MainActivity::class.java)
        val notificationPendingIntent =
            PendingIntent.getActivity(context, ID_REPEATING, notificationIntent, 0)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm_24)
            .setContentTitle(title)
            .setContentText("It's 09.00 AM, Time to Open The App")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun isDateInvalid(time: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }
}
