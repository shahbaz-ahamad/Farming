package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.CartItemBinding
import com.shahbaz.farming.datamodel.CartItem

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.CartItemViewholder>() {


    class CartItemViewholder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: CartItem?) {
            currentItem?.let {
                binding.apply {
                    Glide.with(itemView).load(it.productImage).into(productImage)
                    productTitle.text = it.title
                    productPrice.text = "Rs:${it.price}"
                    productQuantity.text = "Qty: ${it.quantity}"
                    selectedQuantity.text = it.selectedQuantity.toString()
                    if (it.stock == "Out of Stock") {
                        inStock.visibility = View.VISIBLE
                        //disable buynow button
                        buyNow.isEnabled = false
                        //alos change the color
                        buyNow.setBackgroundColor(itemView.context.getColor(R.color.grey))
                    } else {
                        inStock.visibility = View.GONE
                    }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewholder {
        return CartItemViewholder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartItemViewholder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        holder.binding.increaseQuantity.setOnClickListener {
            onPlusClick?.invoke(currentItem)
        }
        holder.binding.decreaseQuantity.setOnClickListener {
            onMinusClick?.invoke(currentItem)
        }
    }

    var onPlusClick : ((CartItem) -> Unit)? = null
    var onMinusClick : ((CartItem) -> Unit)? = null
}