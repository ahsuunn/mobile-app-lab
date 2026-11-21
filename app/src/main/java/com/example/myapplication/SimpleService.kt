package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast

class SimpleService : Service() {
    private var count = 0
    private var isRunning = false
    private var handler = Handler(Looper.getMainLooper())

    private val countRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                count++
                Log.d("Simple Service", "Count: $count")
                handler.postDelayed(this, 1000L)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service Starting", Toast.LENGTH_SHORT).show()
        if (!isRunning)
        {
            isRunning = true
            handler.post(countRunnable)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacks(countRunnable)
        Log.d("SimpleService", "Service Stopped")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}