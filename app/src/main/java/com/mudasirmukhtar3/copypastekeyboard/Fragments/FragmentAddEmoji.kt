package com.sharpforks.copypaste.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sharpforks.copypaste.LocalStorage.AppDatabase
import com.sharpforks.copypaste.LocalStorage.Emojis
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAddEmoji:Fragment() {

    private lateinit var et_text: EditText
    private lateinit var text: TextView
    private lateinit var btn_add: RelativeLayout
    private lateinit var rel_top: RelativeLayout

    private var db: AppDatabase? = null
    private var textDao: TextDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_sentence, container, false)

        db = AppDatabase.getAppDataBase(context = requireContext())
        textDao = db?.textDao()

        text=root.findViewById(R.id.text)
        text.text ="Add Emoji"

        et_text=root.findViewById(R.id.et_text)
        et_text.requestFocus()

        val imgr: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        rel_top=root.findViewById(R.id.rel_top)
        btn_add=root.findViewById(R.id.btn_add)
        btn_add.setOnClickListener {
            var text= et_text.text.toString()
            if(text.equals(""))
            {
                Toast.makeText(requireContext(),"Empty text", Toast.LENGTH_SHORT).show()
            }
            else
            {
                CoroutineScope(Dispatchers.Default).launch {

                    var textorder =0
                    with(textDao) {
                        textorder = this?.getLargestOrder()!!
                    }


                    var order = Emojis(
                        text =text,
                        eOrder = textorder+1
                    )
                    saveEmoji(order)
                }

            }
        }

        rel_top.setOnClickListener {
            et_text.requestFocus()
        }
        return root
    }

    fun saveEmoji(emojis: Emojis){

        with(textDao) {
            this?.insertEmojis(emojis)
        }

        val fragmentEmoji =
            FragmentEmoji()

        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, fragmentEmoji, "FragmentEmoji")
            .addToBackStack(null)
            .commit()
    }
}