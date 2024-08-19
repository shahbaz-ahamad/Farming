package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.AdressLayoutBinding
import com.shahbaz.farming.datamodel.Address


class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewholder>() {

    var selectedPosition = -1 // Initialize with an invalid position

    class AddressViewholder(val binding: AdressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Address?, isSelected: Boolean) {
            currentItem?.let {
                binding.apply {
                    title.text = it.addressTitle
                    //if selected is true then setup other drawable
                    title.setBackgroundResource(
                        if (isSelected) R.drawable.button_drawable
                        else R.drawable.light_gree_with_border
                    )
                }
            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewholder {
        return AddressViewholder(
            AdressLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AddressViewholder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem, position == selectedPosition)
        holder.itemView.setOnClickListener {
            onAddressItemClick?.invoke(currentItem)
            var previouseSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previouseSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    var onAddressItemClick: ((Address) -> Unit)? = null
}