package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.BillingProductBinding
import com.shahbaz.farming.databinding.CartItemBinding
import com.shahbaz.farming.datamodel.CartItem

class BillingProductAdapter :
    RecyclerView.Adapter<BillingProductAdapter.BillingProductViewholder>() {

    class BillingProductViewholder(val binding: BillingProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: CartItem?) {
            currentItem?.let {
                binding.apply {
                    Glide.with(itemView).load(it.productImage).into(productImage)
                    productTitle.text = it.title
                    productPrice.text = "Rs:${it.price}"
                    productQuantity.text = "Qty: ${it.selectedQuantity}"
                }
            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewholder {
        return BillingProductViewholder(
            BillingProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingProductViewholder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

    }

}