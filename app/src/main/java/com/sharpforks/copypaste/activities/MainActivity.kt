package com.sharpforks.copypaste.activities

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.sharpforks.copypaste.Fragments.FragmentAddEmoji
import com.sharpforks.copypaste.Fragments.FragmentAddSentence
import com.sharpforks.copypaste.Fragments.FragmentEmoji
import com.sharpforks.copypaste.Fragments.FragmentText
import com.sharpforks.copypaste.LocalStorage.AppDatabase
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.R


class MainActivity : AppCompatActivity(){

    private lateinit var btn_add_text: ImageView
    private lateinit var imgtext: ImageView
    private lateinit var imgemoji: ImageView

    private lateinit var rel_emoji: RelativeLayout
    private lateinit var rel_text: RelativeLayout

    private lateinit var txt_text: TextView
    private lateinit var txt_emoji: TextView


    private var db: AppDatabase? = null
    private var textDao: TextDao? = null

    var selectedtab ="text"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_add_text = findViewById(R.id.btn_add_text)
        imgemoji = findViewById(R.id.imgemoji)
        imgtext = findViewById(R.id.imgtext)

        rel_emoji = findViewById(R.id.rel_emoji)
        rel_text = findViewById(R.id.rel_text)

        txt_text = findViewById(R.id.txt_text)
        txt_emoji = findViewById(R.id.txt_emoji)

        rel_text.setOnClickListener {
            selectedtab ="text"

            setImageResource(this,imgtext,"text_icon_selected")
            setImageResource(this,imgemoji,"emoji_icon_unselected")

            setTextAppearance(this,txt_text,R.style.style_sf_pro_xlarge_bold_primary)
            setTextAppearance(this,txt_emoji,R.style.style_sf_pro_xlarge_bold_grey)

            val textFragment =
                FragmentText()
            val transaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, textFragment, "Text")
            transaction.addToBackStack(null)
            transaction.commit()

        }

        rel_emoji.setOnClickListener {
            selectedtab ="emoji"

            setImageResource(this,imgtext,"text_icon_unselected")
            setImageResource(this,imgemoji,"emoji_icon_selected")
            setTextAppearance(this,txt_text,R.style.style_sf_pro_xlarge_bold_grey)
            setTextAppearance(this,txt_emoji,R.style.style_sf_pro_xlarge_bold_primary)

            val emojiFragment =
                FragmentEmoji()
            val transaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, emojiFragment, "Emoji")
            transaction.addToBackStack(null)
            transaction.commit()

        }

        btn_add_text.setOnClickListener {

            if(selectedtab.equals("text")) {
                val fragmentAddSentence =
                    FragmentAddSentence()

                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragmentAddSentence, "FragmentAddSentence")
                    .addToBackStack(null)
                    .commit();
            }

            if(selectedtab.equals("emoji"))
            {
                val fragmentEmoji =
                    FragmentAddEmoji()

                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragmentEmoji, "FragmentAddEmojis")
                    .addToBackStack(null)
                    .commit();
            }


        }

        db = AppDatabase.getAppDataBase(context = this)
        textDao = db?.textDao()

        val textFragment =
            FragmentText()
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, textFragment, "Text")
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun setTextAppearance(context: Context, textView: TextView, style:Int){
        textView.setTextAppearance(context, style)
    }

    fun setImageResource(context: Context,img:ImageView ,drawable: String){
        img.setImageResource(getImage(context,drawable))
    }

    fun getImage(context: Context,imageName: String?): Int {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName())
    }

}