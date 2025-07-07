package com.sharpforks.copypaste.ViewModel

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharpforks.copypaste.R
import com.sharpforks.copypaste.listeners.ItemClick

class keyboardVH(itemView: View, click: ItemClick, type: Int) :
    RecyclerView.ViewHolder(itemView) {
    var text: TextView? = itemView.findViewById(R.id.text)

    init {
        itemView.setOnClickListener {
            click.item(adapterPosition, type)
        }
    }
}