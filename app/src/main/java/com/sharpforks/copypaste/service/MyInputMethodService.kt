package com.sharpforks.copypaste.service

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharpforks.copypaste.LocalStorage.AppDatabase
import com.sharpforks.copypaste.LocalStorage.Emojis
import com.sharpforks.copypaste.LocalStorage.Text
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.R
import com.sharpforks.copypaste.ViewModel.KeyboardViewModel
import com.sharpforks.copypaste.adapters.keyboardAdapter
import com.sharpforks.copypaste.listeners.ItemClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener,
    ItemClick {

    private var keyboardView: KeyboardView? = null
    private var keyboard: Keyboard? = null
    private lateinit var keyboardAdapter: keyboardAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var caps = false

    var dataList: ArrayList<KeyboardViewModel> = ArrayList()

    private var db: AppDatabase? = null
    private var textDao: TextDao? = null

    var textsList = arrayOf(
        "What’s up?",
        "⁉️ I MISS THE RAGE ⁉️",
        "🧛🏻SLATT 👺 SLATT",
        "𝕸𝖎𝖘𝖘 𝖞𝖔𝖚",
        "𝕀 𝕝𝕠𝕧𝕖 𝕪𝕠𝕦 ❤️",
        "example@email.com",
        "(213) 123-1234",
        "/diamonds"
    )
    val emojislist = arrayOf(
        "٩(˘◡˘)۶",
        "(•◡•)",
        "(ɔ◔‿◔)ɔ",
        "❀◕‿◕❀",
        "(｡◕‿◕｡)",
        "●‿●",
        "◕‿◕",
        "ツ",
        "(✿ヘᴥヘ)",
        "🌘‿🌘",
        "(づ｡◕‿‿◕｡)づ",
        "(/◔◡◔)/",
        "s(^‿^)-b",
        "(人◕‿◕)",
        "(✿╹◡╹)",
        "◔◡◔",
        "(^▽^)",
        "(✿^▽^)",
        "ᵔ⌣ᵔ",
        "ᵔᴥᵔ",
        "(≧◡≦)",
        "^ㅅ^",
        "^ㅂ^",
        "(⌒‿⌒)",
        "◠◡◠",
        "⁀‿⁀",
        "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧",
        "(✿◕ᗜ◕)━♫.*･｡ﾟ",
        "ᕕ(ᐛ)ᕗ",
        "♡",
        "❤",
        "♥",
        "❥",
        "💘",
        "💙",
        "💗",
        "💖",
        "💕",
        "💓",
        "💞",
        "💝",
        "💟"
    )


    override fun swipeRight() {
    }

    override fun onPress(p0: Int) {
    }

    override fun onRelease(p0: Int) {
    }

    override fun swipeLeft() {
    }

    override fun swipeUp() {
    }

    override fun swipeDown() {
    }

    override fun onKey(primaryCode: Int, p1: IntArray?) {
        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            when (primaryCode) {
                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                    caps = !caps
                    keyboard!!.isShifted = caps
                    keyboardView!!.invalidateAllKeys()
                }
                Keyboard.KEYCODE_SHIFT -> {
                    caps = !caps
                    keyboard!!.isShifted = caps
                    keyboardView!!.invalidateAllKeys()
                }
                Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )
                else -> {
                    var code = primaryCode as Char
                    if (Character.isLetter(code) && caps) {
                        code = Character.toUpperCase(code)
                    }
                    inputConnection.commitText(code.toString(), 1)
                }
            }
        }
    }

    override fun onText(p0: CharSequence?) {
    }


    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getAppDataBase(context = this)
        textDao = db?.textDao()

        for (i in 0..textsList.size - 1) {
            saveText(textsList.get(i))
        }

        for (i in 0..emojislist.size - 1) {
            saveEmoji(emojislist.get(i))
        }
    }

    fun saveEmoji(text: String) {
        CoroutineScope(Dispatchers.Default).launch {
            with(textDao) {
                var exist = this?.isEmojiIsExist(text)
                if (!exist!!) {
                    var emojiOrder = 0
                    with(textDao) {
                        emojiOrder = this?.getLargestOrderEmoji()!!
                    }
                    var emojis = Emojis(
                        text = text,
                        eOrder = emojiOrder + 1
                    )
                    with(textDao) {
                        this?.insertEmojis(emojis)
                    }
                }
            }
        }
    }


    fun saveText(text: String) {
        CoroutineScope(Dispatchers.Default).launch {

            with(textDao) {
                var exist = this?.isTextIsExist(text)
                if (!exist!!) {
                    var textorder = 0
                    with(textDao) {
                        textorder = this?.getLargestOrder()!!
                    }

                    var order = Text(
                        text = text,
                        tOrder = textorder + 1
                    )

                    with(textDao) {
                        Log.e("TEXTTTTTT insert ", text)

                        this?.insert(order)
                    }
                }
            }

        }
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        return false
    }

    fun getEmojiList() {
        dataList.clear()

        CoroutineScope(Dispatchers.Default).launch {
            var cart: List<Emojis>? = db?.textDao()?.getAllEmojis()

            Log.e("DATAAAA ", " getdata " + cart!!.size)

            for (i in 0..cart!!.size - 1) {
                var txt = cart!!.get(i).text

                var model = KeyboardViewModel()
                model.id = cart!!.get(i).pid
                model.text = cart!!.get(i).text

                dataList.add(model)

                Log.e("DATAAAA ", " getdata " + txt)
            }
            CoroutineScope(Dispatchers.Main).launch {

                keyboardAdapter.notifyDataSetChanged()
            }

        }
    }

    fun getTextList() {
        dataList.clear()

        CoroutineScope(Dispatchers.Default).launch {
            var cart: List<Text>? = db?.textDao()?.getAll()

            Log.e("DATAAAA ", " getdata " + cart!!.size)

            for (i in 0..cart!!.size - 1) {
                var txt = cart!!.get(i).text

                var model = KeyboardViewModel()
                model.id = cart!!.get(i).pid
                model.text = cart!!.get(i).text

                dataList.add(model)

                Log.e("DATAAAA ", " getdata " + txt)
            }
            CoroutineScope(Dispatchers.Main).launch {

                keyboardAdapter.notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateInputView(): View {

        var selected = "text"

        var View = layoutInflater.inflate(R.layout.keyboard_view, null)
        var recyclerView = View.findViewById(R.id.recyclerView) as RecyclerView
        var img_space = View.findViewById(R.id.img_space) as ImageView
        var img_cross = View.findViewById(R.id.img_cross) as ImageView
        var img_return = View.findViewById(R.id.img_return) as ImageView
        //var btn = View.findViewById(R.id.btn) as Button
        var img_txt_emoji = View.findViewById(R.id.img_txt_emoji) as ImageView

        img_txt_emoji.setOnClickListener {

            if (selected.contains("text")) {
                Log.e("IMGGGGG", "text")

                // btn.text = "EMOJI"
                setImageResource(applicationContext, img_txt_emoji, "emoji_btn")

                selected = "emoji"

                getEmojiList()
            } else {
                Log.e("IMGGGGG", "emojii")

                // btn.text = "ABC"

                setImageResource(applicationContext, img_txt_emoji, "abc")

                selected = "text"
                getTextList()
            }
        }

//        btn.setOnClickListener {
//            Log.e("IMGGGGG", "img_txt_emoji" + selected)
//
//
//
//        }

        img_space.setOnClickListener {
            val inputConnection = currentInputConnection

            inputConnection.commitText(" ", 1)

        }
        var handler = Handler()
        val myRunnable: Runnable = object : Runnable {
            override fun run() {
                val inputConnection = currentInputConnection
                inputConnection.deleteSurroundingText(1, 0)
                val selectedText = inputConnection.getSelectedText(0)

                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0)
                } else {
                    inputConnection.commitText("", 1)
                }
                handler.postDelayed(this, 100)
            }
        }

        img_cross.setOnLongClickListener {
            handler.postDelayed(myRunnable, 100)
            return@setOnLongClickListener true
        }

        img_cross.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(myRunnable)

            }
            return@setOnTouchListener false
        }

        img_cross.setOnClickListener {

            val inputConnection = currentInputConnection
            val selectedText = inputConnection.getSelectedText(0)

            if (TextUtils.isEmpty(selectedText)) {
                inputConnection.deleteSurroundingText(1, 0)
            } else {
                inputConnection.commitText("", 1)
            }
        }

        img_return.setOnClickListener {
            val inputConnection = currentInputConnection

            inputConnection.sendKeyEvent(
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_ENTER
                )
            )
        }

        linearLayoutManager = LinearLayoutManager(this)
        var gridLayoutManagerCategories =
            GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManagerCategories
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        keyboardAdapter = keyboardAdapter(
            this,
            dataList,
            this,
            10,
            0
        )
        recyclerView.adapter = keyboardAdapter

        getTextList()

        Log.e("KEYBOARDDD ", " getkeyboarddata  " + dataList?.size)

        return View
    }


    override fun item(obj: Any, type: Int) {
        var text = dataList[obj as Int].text

        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            inputConnection.commitText(text, 1)
        }
    }


    fun setImageResource(context: Context, img: ImageView, drawable: String) {
        img.setImageResource(getImage(context, drawable))
    }

    fun getImage(context: Context, imageName: String?): Int {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName())
    }
}