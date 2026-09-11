/*
 * Copyright (c) 2022 Auxio Project
 * DetailListAdapter.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.detail.list

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.databinding.ItemSortHeaderBinding
import org.oxycblt.auxio.list.BasicHeader
import org.oxycblt.auxio.list.Item
import org.oxycblt.auxio.list.PlainDivider
import org.oxycblt.auxio.list.PlainHeader
import org.oxycblt.auxio.list.SelectableListListener
import org.oxycblt.auxio.list.adapter.SelectionIndicatorAdapter
import org.oxycblt.auxio.list.adapter.SimpleDiffCallback
import org.oxycblt.auxio.list.recycler.BasicHeaderViewHolder
import org.oxycblt.auxio.list.recycler.DividerViewHolder
import org.oxycblt.auxio.util.context
import org.oxycblt.auxio.util.inflater
import org.oxycblt.musikr.Music

abstract class DetailListAdapter(
    private val listener: Listener<*>,
    private val diffCallback: DiffUtil.ItemCallback<Item>,
) : SelectionIndicatorAdapter<Item, RecyclerView.ViewHolder>(diffCallback) {

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {
            is PlainDivider -> DividerViewHolder.VIEW_TYPE
            is BasicHeader -> BasicHeaderViewHolder.VIEW_TYPE
            is SortHeader -> SortHeaderViewHolder.VIEW_TYPE
            else -> super.getItemViewType(position)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            DividerViewHolder.VIEW_TYPE ->
                DividerViewHolder.from(parent).apply {
                    itemView.layoutParams =
                        RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT,
                        )
                }
            BasicHeaderViewHolder.VIEW_TYPE -> BasicHeaderViewHolder.from(parent)
            SortHeaderViewHolder.VIEW_TYPE -> SortHeaderViewHolder.from(parent)
            else -> error("Invalid item type $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is BasicHeader -> (holder as BasicHeaderViewHolder).bind(item)
            is SortHeader -> (holder as SortHeaderViewHolder).bind(item, listener)
        }
    }

    interface Listener<in T : Music> : SelectableListListener<T> {
        fun onOpenSortMenu()
    }

    protected companion object {
        val DIFF_CALLBACK =
            object : SimpleDiffCallback<Item>() {
                override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                    return when {
                        oldItem is PlainDivider && newItem is PlainDivider ->
                            DividerViewHolder.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem)
                        oldItem is BasicHeader && newItem is BasicHeader ->
                            BasicHeaderViewHolder.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem)
                        oldItem is SortHeader && newItem is SortHeader ->
                            SortHeaderViewHolder.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem)
                        else -> false
                    }
                }
            }
    }
}

data class SortHeader(@StringRes override val titleRes: Int) : PlainHeader

private class SortHeaderViewHolder(private val binding: ItemSortHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(sortHeader: SortHeader, listener: DetailListAdapter.Listener<*>) {
        binding.headerTitle.text = binding.context.getString(sortHeader.titleRes)
        binding.headerSort.apply {
            TooltipCompat.setTooltipText(this, contentDescription)
            setOnClickListener { listener.onOpenSortMenu() }
        }
    }

    companion object {
        const val VIEW_TYPE = IntegerTable.VIEW_TYPE_SORT_HEADER

        fun from(parent: ViewGroup) =
            SortHeaderViewHolder(
                ItemSortHeaderBinding.inflate(parent.context.inflater, parent, false)
            )

        val DIFF_CALLBACK =
            object : SimpleDiffCallback<SortHeader>() {
                override fun areContentsTheSame(oldItem: SortHeader, newItem: SortHeader) =
                    oldItem.titleRes == newItem.titleRes
            }
    }
}
