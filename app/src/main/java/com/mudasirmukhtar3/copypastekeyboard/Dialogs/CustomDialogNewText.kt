package com.sharpforks.copypaste.Dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import com.sharpforks.copypaste.listeners.addNewTextListener
import com.sharpforks.copypaste.R

class CustomDialogNewText
    (c: Activity,var addNewTextListener: addNewTextListener,selectedtab:String) : Dialog(c), View.OnClickListener {

    private var mcallback: addNewTextListener? = null

    var tab=selectedtab

    private lateinit var reladd:RelativeLayout
    private lateinit var et:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_new_text)

        mcallback = addNewTextListener

        et = findViewById(R.id.et)
        reladd = findViewById(R.id.reladd)
        reladd.setOnClickListener(this
        )

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.reladd ->
            {
                var text = et.text.toString()
                dismiss()

                mcallback!!.newText(text,tab)
            }
        }
        dismiss()
    }
}