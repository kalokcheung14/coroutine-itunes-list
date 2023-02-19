package com.kalok.simpleituneslist.adapters

import androidx.recyclerview.widget.DiffUtil

class ItemListDiffUtil<Album>(private val oldItems: ArrayList<Album>, private val newItems: ArrayList<Album>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition] == newItems[newItemPosition]
}