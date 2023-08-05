package com.sharpforks.copypaste.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sharpforks.copypaste.ItemMoveCallback
import com.sharpforks.copypaste.LocalStorage.AppDatabase
import com.sharpforks.copypaste.LocalStorage.Text
import com.sharpforks.copypaste.LocalStorage.TextDao
import com.sharpforks.copypaste.ViewModel.KeyboardViewModel
import com.sharpforks.copypaste.ViewModel.keyboardVH
import com.sharpforks.copypaste.listeners.ItemClick
import com.sharpforks.copypaste.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class TextListAdapter(
    var context: Context,
    var dataList: ArrayList<KeyboardViewModel> = ArrayList(),
    var itemClick: ItemClick,
    var type: Int,
    var selectedItem: Int
) :
    RecyclerView.Adapter<keyboardVH>(), ItemMoveCallback.ItemTouchHelperContract {

    private var db: AppDatabase? = null
    private var textDao: TextDao? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): keyboardVH {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_text_list, viewGroup, false)

        db = AppDatabase.getAppDataBase(context = context)
        textDao = db?.textDao()

        return keyboardVH(
            view,
            itemClick,
            type
        )
    }

    override fun getItemCount() = dataList!!.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: keyboardVH, position: Int) {
        Log.e("KEYBOARDDD ", " onBindViewHolder  " + dataList?.get(holder.adapterPosition).text)
        holder.text?.text = dataList?.get(holder.adapterPosition).text
    }


    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        Log.e("ROWWWW", " onRowMoved  fromPosition " + fromPosition + " toPosition " + toPosition)
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)

    }

    override fun onRowSelected(myViewHolder: keyboardVH?) {
        Log.e("ROWWWW", " onRowMoved  onRowSelected")

    }

    override fun onRowClear(myViewHolder: keyboardVH?) {
        Log.e("ROWWWW", " onRowMoved  onRowClear")

        var dataListOrder: ArrayList<Text> = ArrayList()

        CoroutineScope(Dispatchers.Default).launch {

            with(textDao) {
                this?.delete()
            }

            for(i in 0..dataList.size-1)
            {
                Log.e("ROWWWW", " onRowMoved  textttt " + dataList[i].text + "  order "+ dataList[i].order)

                var textorder =0
                with(textDao) {
                    textorder = this?.getLargestOrder()!!
                }

                var order = Text(
                        text = dataList[i].text,
                        tOrder = textorder+1
                    )

                with(textDao) {
                    this?.insert(order)
                }

            }
        }

    }

}