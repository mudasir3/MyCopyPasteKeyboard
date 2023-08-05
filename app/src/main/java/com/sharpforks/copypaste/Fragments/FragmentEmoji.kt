package com.sharpforks.copypaste.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharpforks.copypaste.DividerItemDecorator
import com.sharpforks.copypaste.ItemMoveCallback
import com.sharpforks.copypaste.LocalStorage.AppDatabase
import com.sharpforks.copypaste.LocalStorage.Emojis
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.ViewModel.KeyboardViewModel
import com.sharpforks.copypaste.listeners.ItemClick
import com.sharpforks.copypaste.R
import com.sharpforks.copypaste.adapters.EmojisListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentEmoji : Fragment(), ItemClick {

    private lateinit var emojisListAdapter: EmojisListAdapter
    private lateinit var recycler_emojis: RecyclerView
    var dataList: ArrayList<KeyboardViewModel> = ArrayList()

    private var db: AppDatabase? = null
    private var textDao: TextDao? = null

    val emojislist = arrayOf("٩(˘◡˘)۶", "(•◡•)", "(ɔ◔‿◔)ɔ", "❀◕‿◕❀", "(｡◕‿◕｡)", "●‿●", "◕‿◕", "ツ", "(✿ヘᴥヘ)", "🌘‿🌘", "(づ｡◕‿‿◕｡)づ", "(/◔◡◔)/", "s(^‿^)-b", "(人◕‿◕)", "(✿╹◡╹)", "◔◡◔","(^▽^)", "(✿^▽^)", "ᵔ⌣ᵔ", "ᵔᴥᵔ", "(≧◡≦)", "^ㅅ^", "^ㅂ^", "(⌒‿⌒)", "◠◡◠", "⁀‿⁀","(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", "(✿◕ᗜ◕)━♫.*･｡ﾟ", "ᕕ(ᐛ)ᕗ","♡", "❤", "♥", "❥", "💘", "💙", "💗", "💖", "💕", "💓", "💞", "💝", "💟")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_emojis, container, false)

        db = AppDatabase.getAppDataBase(context = requireContext())
        textDao = db?.textDao()

        for(i in 0..emojislist.size-1) {
            saveEmoji(emojislist.get(i))
        }

        hideSoftKeyboard(requireActivity())

        recycler_emojis = root.findViewById(R.id.recycler_emojis)
        var gridLayoutManagerCategories =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        recycler_emojis.layoutManager = gridLayoutManagerCategories

        val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.divider_drawable
            )!!
        )
        recycler_emojis.addItemDecoration(dividerItemDecoration)


        emojisListAdapter = EmojisListAdapter(
            requireContext(),
            dataList,
            this,
            10,
            0
        )

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(emojisListAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recycler_emojis)

        recycler_emojis.adapter = emojisListAdapter

        getdata()
        return root
    }

    fun saveEmoji(text:String) {
        CoroutineScope(Dispatchers.Default).launch {
            with(textDao) {
                var exist = this?.isEmojiIsExist(text)
                if (!exist!!) {
                    var emojiorder = 0
                    with(textDao) {
                        emojiorder = this?.getLargestOrder()!!
                    }

                    var emojis = Emojis(
                        text = text,
                        eOrder = emojiorder + 1
                    )
                    with(textDao) {
                        this?.insertEmojis(emojis)
                    }
                }
            }

        }
    }

    fun getdata() {

        dataList.clear()

        CoroutineScope(Dispatchers.Default).launch {
            var cart: List<Emojis>? = db?.textDao()?.getAllEmojis()

            Log.e("DATAAAA ", " getdata " + cart!!.size)

            for (i in 0..cart.size - 1) {
                var txt = cart.get(i).text

                var model = KeyboardViewModel()
                model.id = cart.get(i).pid
                model.text = cart.get(i).text
                dataList.add(model)

                Log.e("DATAAAA ", " getdata " + txt)
            }
            CoroutineScope(Dispatchers.Main).launch {

                emojisListAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun item(obj: Any, type: Int) {

    }


    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}