package com.idnsoft.githubusers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val SHARED_PREFERENCE = "sharedpreference"
        const val SWITCH_KEY = "switchkey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = getString(R.string.setting)

        alarmReceiver = AlarmReceiver()


        btn_change_language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val getSwicthCheck = sharedPreferences.getBoolean(SWITCH_KEY, false)
        sw_notification.isChecked = getSwicthCheck

        sw_notification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(SWITCH_KEY, true)
                }.apply()

                alarmReceiver.setDialyReminder(this)
            } else {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(SWITCH_KEY, false)
                }.apply()

                alarmReceiver.cancelDialyReminder(this)
            }
        }
    }
}