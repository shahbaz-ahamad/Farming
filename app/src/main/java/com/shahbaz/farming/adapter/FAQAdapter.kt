package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FaqItemBinding
import com.shahbaz.farming.datamodel.FAQ

class FAQAdapter : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {


    class FAQViewHolder(val binding: FaqItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(faq: FAQ?) {
            faq?.let {
                binding.question.text = it.question
                binding.answer.text = it.answer
                if (faq.isExpanded) {
                    binding.answer.visibility = View.VISIBLE
                    binding.arrowIcon.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                } else {
                    binding.answer.visibility = View.GONE
                    binding.arrowIcon.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<FAQ>() {
        override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        return FAQViewHolder(
            FaqItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq = differ.currentList[position]
        holder.bind(faq)
        holder.itemView.setOnClickListener {
            // Loop through the list and collapse all items
            differ.currentList.forEachIndexed { index, item ->
                if (index != position && item.isExpanded) {
                    item.isExpanded = false
                    notifyItemChanged(index)
                }
            }

            // Expand or collapse the clicked item
            faq.isExpanded = !faq.isExpanded
            notifyItemChanged(position)
        }

    }
}