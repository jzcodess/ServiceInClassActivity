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
    private var bindService = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            timerBinder = service as TimerService.TimerBinder
            bindService = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bindService = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (bindService) {
                timerBinder.start(10)
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if (bindService) {
                timerBinder.pause()
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (bindService) {
                timerBinder.stop()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bindService) {
            unbindService(serviceConnection)
            bindService = false
        }
    }
}