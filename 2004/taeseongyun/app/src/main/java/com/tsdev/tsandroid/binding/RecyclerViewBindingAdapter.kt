package com.tsdev.tsandroid.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsdev.domain.model.DomainItem
import com.tsdev.tsandroid.ui.adapter.MovieRecyclerAdapter

@BindingAdapter(value = ["bindingItems"])
fun RecyclerView.setBindingListItem(items: List<DomainItem>?) {
    val movieAdapter = adapter as? MovieRecyclerAdapter
    items?.let {
        movieAdapter?.run {
            addItems(it)
            notifiedDataChange()
        }
    }
}