package com.bed.android.bedrock.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.databinding.ListItemPriceBinding
import com.bed.android.bedrock.model.Store

class StoreAdapter(
    diffCallback: DiffUtil.ItemCallback<Store>,
    private val onClick: (Store) -> Unit
) : ListAdapter<Store, StoreViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ListItemPriceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

class StoreViewHolder(private val binding: ListItemPriceBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Store, onClick: (Store) -> Unit) {
        binding.root.setOnClickListener { onClick(item) }
        binding.item = item
    }
}