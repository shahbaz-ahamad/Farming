package com.shahbaz.farming.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.CategoryListLayoutBinding

class CatgoryApdater(val list: ArrayList<String>) :
    RecyclerView.Adapter<CatgoryApdater.categoryViewholder>() {

    var selectedPosition = -1 // Initialize with an invalid position

    class categoryViewholder(val binding: CategoryListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String,isSelected:Boolean) {
            binding.category.text = item
            binding.category.setBackgroundColor(
                if (isSelected) binding.root.context.getColor(R.color.green)
                else binding.root.context.getColor(R.color.light_green)
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryViewholder {
        return categoryViewholder(
            CategoryListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: categoryViewholder, position: Int) {
        val item = list[position]
        holder.bind(item,position==selectedPosition)
        holder.itemView.setOnClickListener {
            onCategoryClick?.invoke(item)
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

   var onCategoryClick : ((String) -> Unit)? = null
}