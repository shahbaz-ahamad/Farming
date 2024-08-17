package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.YourProductItemBinding
import com.shahbaz.farming.datamodel.Product

class YourProductAdaapter() : RecyclerView.Adapter<YourProductAdaapter.yourProductViewholder>() {

    class yourProductViewholder(val binding: YourProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: Product?) {
            productItem?.let {
                binding.apply {
                    Glide.with(itemView).load(productItem.productImage).into(productImage)
                    productTitle.text = productItem.title
                    productPrice.text = "Rs:${productItem.price}"
                    productQuantity.text = "Qty:${productItem.quantity}"
                }
            }
        }

    }


    private val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): yourProductViewholder {
        return yourProductViewholder(
            YourProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: yourProductViewholder, position: Int) {
        val productItem = differ.currentList[position]
        holder.bind(productItem)
    }
}