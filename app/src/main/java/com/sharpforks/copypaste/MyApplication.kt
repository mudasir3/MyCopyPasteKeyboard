package com.sharpforks.copypaste

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sharpforks.copypaste.activities.MainActivity
import com.sharpforks.copypaste.activities.onBoarding.onBoarding1

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("APPLICATION"," ONCREATE")

        val prefs =
            this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        var firstlogin =prefs.getBoolean("firstlogin",true)

        if(firstlogin)
        {
            val intent = Intent(this, onBoarding1::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        else
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}