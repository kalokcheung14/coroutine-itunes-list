package com.kalok.coroutineituneslist.adapters

import androidx.recyclerview.widget.DiffUtil

class ItemListDiffUtil<T>(private val oldItems: ArrayList<T>, private val newItems: ArrayList<T>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition] == newItems[newItemPosition]
}