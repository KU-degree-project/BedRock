package com.bed.android.bedrock.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bed.android.bedrock.R

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME=500;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },SPLASH_VIEW_TIME.toLong())

    }
}