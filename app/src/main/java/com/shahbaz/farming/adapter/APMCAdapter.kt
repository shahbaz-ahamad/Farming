package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.databinding.MarketPriceLayoutBinding
import com.shahbaz.farming.datamodel.apmc.APMCRecords

class APMCAdapter : RecyclerView.Adapter<APMCAdapter.APMCViewHolder>() {

    class APMCViewHolder(val binding: MarketPriceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: APMCRecords?) {
            currentItem?.let { apmcRecord ->
                binding.apmcNameValue.text = apmcRecord.state
                binding.minvalue.text = apmcRecord.min_price
                binding.maxvalue.text = apmcRecord.max_price
                binding.comodityname.text = apmcRecord.commodity
                binding.apmcLocationValue.text = apmcRecord.market

            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<APMCRecords>() {
        override fun areItemsTheSame(oldItem: APMCRecords, newItem: APMCRecords): Boolean {
            return oldItem.commodity == newItem.commodity
        }

        override fun areContentsTheSame(oldItem: APMCRecords, newItem: APMCRecords): Boolean {
            return oldItem == newItem
        }

    }

    val difffer = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APMCViewHolder {
        return APMCViewHolder(
            MarketPriceLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return difffer.currentList.size
    }

    override fun onBindViewHolder(holder: APMCViewHolder, position: Int) {
        val currentItem = difffer.currentList[position]
        holder.bind(currentItem)
    }

}
