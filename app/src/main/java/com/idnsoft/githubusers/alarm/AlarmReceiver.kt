package com.idnsoft.githubusers.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.core.content.res.TypedArrayUtils.getText
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.activity.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ID_REPEATING = 101
        const val TIME_REMINDER = "09:00"

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "ilhmdhn channel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        showDialy(context)
    }

    fun showDialy(context: Context) {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_splash)
            .setContentTitle("Github User App")
            .setContentText("Open App")
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            channel.description = CHANNEL_NAME

            mBuilder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    fun setDialyReminder(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val timeArray =
            TIME_REMINDER.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, R.string.reminder_on, Toast.LENGTH_SHORT).show()

    }

    fun cancelDialyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, R.string.reminder_off, Toast.LENGTH_SHORT).show()
    }

}