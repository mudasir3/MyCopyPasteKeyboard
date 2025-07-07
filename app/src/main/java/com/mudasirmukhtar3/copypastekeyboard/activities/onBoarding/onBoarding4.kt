package com.sharpforks.copypaste.activities.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sharpforks.copypaste.R
import com.sharpforks.copypaste.activities.MainActivity

class onBoarding4 : AppCompatActivity() {
    private lateinit var txt_next: TextView
    private lateinit var txtskip: TextView
    private lateinit var imgback: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding4)

        txt_next = findViewById(R.id.txt_next)
        txtskip = findViewById(R.id.txtskip)
        imgback = findViewById(R.id.imgback)
        imgback.setOnClickListener {
            finish()
        }
        txt_next.setOnClickListener {

            val prefs =
                this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            var editor = prefs.edit()
            editor.putBoolean("firstlogin",false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        txtskip.setOnClickListener {
            val prefs =
                this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            var editor = prefs.edit()
            editor.putBoolean("firstlogin",false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}