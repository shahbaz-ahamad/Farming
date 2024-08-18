package com.shahbaz.farming.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.OtherProductItemBinding
import com.shahbaz.farming.datamodel.Product

class OtherProductAdapter : RecyclerView.Adapter<OtherProductAdapter.OtherProductViewholder>() {
    class OtherProductViewholder(val binding: OtherProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product?) {

            Log.d("item",item.toString())
            item?.let {
                binding.apply {
                    Glide.with(itemView).load(item.productImage).into(productImage)
                    productTitle.text = item.title
                    productPrice.text = "Rs:${item.price}"
                    productQuantity.text = "Qty:${item.quantity}"
                    if(item.stock == "Out of Stock"){
                        inStock.visibility = View.VISIBLE
                    }else{
                        inStock.visibility = View.GONE
                    }
                }
            }

        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherProductViewholder {
        return OtherProductViewholder(
            OtherProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("Adapter", "Item count: ${differ.currentList.size}")
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OtherProductViewholder, position: Int) {
        Log.d("onBindMethod called","yes")
        val item = differ.currentList[position]
        Log.d("Adapter", "Binding item at position $position: $item")
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick : ((Product) -> Unit)? = null
}