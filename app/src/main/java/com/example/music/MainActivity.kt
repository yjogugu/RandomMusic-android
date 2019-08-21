package com.example.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        mainLayout.setPadding(0, statusBarHeight(this), 0, 0)


        val exec = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("현재기분 측정")
            .setDescription("")
            .setNegativeButton("지문센터 터치", exec, DialogInterface.OnClickListener { dialog, which -> }).build()

        val activity = this
        start.setOnClickListener {
            biometricPrompt.authenticate(CancellationSignal(), exec, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val datas = listOf("b", "e", "p")
                    val random = Random().nextInt(datas.size)

                    activity.runOnUiThread(Runnable {
                        intent = Intent(activity,Music::class.java)
                        intent.putExtra("genre",datas[random])
                        Log.e("1",datas[random]+"")
                        startActivity(intent)
                    })
                }
            })

        }

    }

}


