package com.dhimas.dhiflix.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.main.MainActivity
import com.dhimas.dhiflix.utils.Const

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, Const.SPLASH_TIME)

    }

}