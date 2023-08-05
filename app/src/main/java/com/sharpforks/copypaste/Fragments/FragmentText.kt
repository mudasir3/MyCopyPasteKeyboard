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
import com.sharpforks.copypaste.LocalStorage.Text
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.ViewModel.KeyboardViewModel
import com.sharpforks.copypaste.listeners.ItemClick
import com.sharpforks.copypaste.R
import com.sharpforks.copypaste.adapters.TextListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentText: Fragment() , ItemClick {

    private lateinit var textListAdapter: TextListAdapter
    private lateinit var recyclerView: RecyclerView
    var dataList: ArrayList<KeyboardViewModel> = ArrayList()

    private var db: AppDatabase? = null
    private var textDao: TextDao? = null

    var textsList = arrayOf("What’s up?", "⁉️ I MISS THE RAGE ⁉️", "🧛🏻SLATT 👺 SLATT", "𝕸𝖎𝖘𝖘 𝖞𝖔𝖚", "𝕀 𝕝𝕠𝕧𝕖 𝕪𝕠𝕦 ❤️", "example@email.com", "(213) 123-1234", "/diamonds")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_text, container, false)

        db = AppDatabase.getAppDataBase(context = requireContext())
        textDao = db?.textDao()

        hideSoftKeyboard(requireActivity())

        for(i in 0..textsList.size-1) {
            saveText(textsList.get(i))
        }

        recyclerView = root.findViewById(R.id.recycler_texts)



        var gridLayoutManagerCategories =
            GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManagerCategories

        val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.divider_drawable
            )!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        textListAdapter = TextListAdapter(
            requireContext(),
            dataList,
            this,
            10,
            0
        )

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(textListAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = textListAdapter

        getdata()
        return root
    }

    fun saveText(text:String){
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
                        Log.e("TEXTTTTTT insert " , text)

                        this?.insert(order)
                    }
                }
            }

        }
    }

    fun getdata(){
        dataList.clear()

        CoroutineScope(Dispatchers.Default).launch {
            var cart: List<Text>? = db?.textDao()?.getAll()

            Log.e("DATAAAA " , " getdata " + cart!!.size)

            for (i in 0..cart.size - 1) {
                var txt = cart.get(i).text

                var model = KeyboardViewModel()
                model.id = cart.get(i).pid
                model.text = cart.get(i).text
                model.order=cart.get(i).tOrder
                dataList.add(model)

                Log.e("DATAAAA " , " getdata " + txt)

            }
            CoroutineScope(Dispatchers.Main).launch {

                textListAdapter.notifyDataSetChanged()
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