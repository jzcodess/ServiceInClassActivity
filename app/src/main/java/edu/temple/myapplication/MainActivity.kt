package edu.temple.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var timerBinder: TimerService.TimerBinder
    private var bindservice = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            timerBinder = service as TimerService.TimerBinder
            bindservice = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bindservice = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (bindservice) {
                timerBinder.start(10)
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if (bindservice) {
                timerBinder.pause()
            }
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (bindservice) {
                timerBinder.stop()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bindservice) {
            unbindService(connection)
            bindservice = false
        }
    }
}