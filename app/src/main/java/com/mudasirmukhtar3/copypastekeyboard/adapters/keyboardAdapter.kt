package com.sharpforks.copypaste.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sharpforks.copypaste.ViewModel.KeyboardViewModel
import com.sharpforks.copypaste.ViewModel.keyboardVH
import com.sharpforks.copypaste.listeners.ItemClick
import com.sharpforks.copypaste.R


class keyboardAdapter(
    var context: Context,
    var dataList: ArrayList<KeyboardViewModel> = ArrayList(),
    var itemClick: ItemClick,
    var type: Int,
    var selectedItem: Int
) :
    RecyclerView.Adapter<keyboardVH>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): keyboardVH {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_keyboard, viewGroup, false)
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
        Log.e("KEYBOARDDD ", " onBindViewHolder  " +dataList?.get(holder.adapterPosition).text )
        holder.text?.text = dataList?.get(holder.adapterPosition).text
    }

}