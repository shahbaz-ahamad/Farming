package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.OrderItemBinding
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.datamodel.OrderStatus
import com.shahbaz.farming.datamodel.OrderStatus.Cancel.getOrderStatus

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewholder>() {

    class OrderViewholder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Order?) {
            currentItem?.let {
                binding.apply {
                    Glide.with(itemView).load(it.product.productImage).into(productImage)
                    productTitle.text = it.product.title
                    productPrice.text = "Payable Amount:${it.product.price}"
                    productQuantity.text = "Orderd Qty:${it.product.selectedQuantity.toString()}"

                    stepView.setSteps(
                        mutableListOf(
                            OrderStatus.Ordered.status,
                            OrderStatus.Confirmed.status,
                            OrderStatus.Shipped.status,
                            OrderStatus.Delivered.status
                        )
                    )


                    val currentPosition = when (getOrderStatus(it.orderStatus)) {
                        is OrderStatus.Ordered -> 0
                        is OrderStatus.Confirmed -> 1
                        is OrderStatus.Shipped -> 2
                        is OrderStatus.Delivered -> 3
                        else -> 0
                    }
                    stepView.go(currentPosition, false)
                    if (currentPosition == 3) {
                        stepView.done(true)
                    }

                }

            }
        }
    }


    private val diffutil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewholder {
        return OrderViewholder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewholder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        holder.binding.downloadBill.setOnClickListener {
            onDownloadBillClick?.invoke(currentItem)
        }
        holder.binding.trackStatus.setOnClickListener {
            holder.binding.stepView.visibility =
                if (holder.binding.stepView.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

    }

    var onDownloadBillClick: ((Order) -> Unit)? = null
}