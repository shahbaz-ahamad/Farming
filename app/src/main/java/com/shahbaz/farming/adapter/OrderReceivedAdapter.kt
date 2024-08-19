package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.OrderReceivedItemBinding
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.datamodel.OrderStatus
import com.shahbaz.farming.datamodel.OrderStatus.Cancel.getOrderStatus

class OrderReceivedAdapter : RecyclerView.Adapter<OrderReceivedAdapter.OrderReceivedViewHolder>() {

    class OrderReceivedViewHolder(val binding: OrderReceivedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(curerntOrder: Order?) {
            curerntOrder?.let {
                binding.apply {
                    Glide.with(productImage).load(it.product.productImage).into(productImage)
                    productTitle.text = it.product.title
                    productPrice.text = "Rs${it.product.price}"
                    productQuantity.text = "Selected Qty:${it.product.selectedQuantity.toString()}"

                    stepViewOrderRecevied.setSteps(
                        mutableListOf(
                            OrderStatus.Ordered.status,
                            OrderStatus.Confirmed.status,
                            OrderStatus.Shipped.status,
                            OrderStatus.Delivered.status
                        )
                    )

                    val currentStatus = when (getOrderStatus(it.orderStatus)) {
                        is OrderStatus.Ordered -> 0
                        is OrderStatus.Confirmed -> 1
                        is OrderStatus.Shipped -> 2
                        is OrderStatus.Delivered -> 3
                        else -> 0
                    }
                    stepViewOrderRecevied.go(currentStatus, false)
                }
            }
        }

    }


    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val diifer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReceivedViewHolder {
        return OrderReceivedViewHolder(
            OrderReceivedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diifer.currentList.size
    }

    override fun onBindViewHolder(holder: OrderReceivedViewHolder, position: Int) {
        val curerntOrder = diifer.currentList[position]
        holder.bind(curerntOrder)

        holder.binding.updateStatus.setOnClickListener {
            onUpdateClick?.invoke(curerntOrder)

        }
    }


    var onUpdateClick: ((Order) -> Unit)? = null
}