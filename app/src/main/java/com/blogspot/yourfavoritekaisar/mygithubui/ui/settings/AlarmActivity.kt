package com.blogspot.yourfavoritekaisar.mygithubui.ui.settings
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val SHARED_PREFERENCE = "sharedpreference"
        const val BOOLEAN_KEY = "booleankey"
        internal val TAG = AlarmActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarmReceiver = AlarmReceiver()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.alarm)
        }

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val getBoolean = sharedPreferences.getBoolean(BOOLEAN_KEY, false)
        checkbox_alarm.isChecked = getBoolean

        checkbox_alarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, true)
                }.apply()

                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.EXTRA_TYPE, "09:00")
                Log.d(TAG, "Alarm On")
            } else {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN_KEY, false)
                }.apply()

                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
                Log.d(TAG, "Alarm Off")
            }
        }
        constrains.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}