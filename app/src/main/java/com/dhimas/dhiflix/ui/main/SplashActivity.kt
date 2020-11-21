package com.dhimas.dhiflix.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dhimas.dhiflix.R

class SplashActivity : AppCompatActivity() {
    private val preferenceName = "PREFERENCE"
    private val preferencesKey = "IS_FIRST_TIME"
    private lateinit var sharedPreferences: SharedPreferences
    private val splashTime = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

        splashDone()

        if (isFirstTime()) {

        }

    }

    private fun splashDone() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTime)
    }

    private fun setFirstTime() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(preferencesKey, false)
        editor.apply()
    }

    private fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(preferencesKey, true)
    }
}