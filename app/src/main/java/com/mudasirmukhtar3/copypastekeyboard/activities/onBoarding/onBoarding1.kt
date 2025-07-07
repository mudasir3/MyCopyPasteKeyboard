package com.sharpforks.copypaste.activities.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sharpforks.copypaste.R

class onBoarding1 : AppCompatActivity() {
    private lateinit var txt_next:TextView
    private lateinit var txtskip:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding1)

        txt_next = findViewById(R.id.txt_next)
        txtskip = findViewById(R.id.txtskip)
        txt_next.setOnClickListener {
            val intent = Intent(this, onBoarding2::class.java)
            startActivity(intent)
        }

        txtskip.setOnClickListener {
            val intent = Intent(this, onBoarding2::class.java)
            startActivity(intent)
        }
    }
}