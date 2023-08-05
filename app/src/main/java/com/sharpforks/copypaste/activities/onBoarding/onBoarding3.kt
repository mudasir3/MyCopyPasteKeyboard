package com.sharpforks.copypaste.activities.onBoarding

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sharpforks.copypaste.R

class onBoarding3 : AppCompatActivity() {
    private lateinit var txt_next: TextView
    private lateinit var txtskip: TextView
    private lateinit var imgback: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding3)

        txt_next = findViewById(R.id.txt_next)
        txtskip = findViewById(R.id.txtskip)
        imgback = findViewById(R.id.imgback)
        imgback.setOnClickListener {
            finish()
        }
        txt_next.setOnClickListener {
            val intent = Intent(this, onBoarding4::class.java)
            startActivity(intent)
        }

        txtskip.setOnClickListener {
            val intent = Intent(this, onBoarding4::class.java)
            startActivity(intent)
        }
    }
}