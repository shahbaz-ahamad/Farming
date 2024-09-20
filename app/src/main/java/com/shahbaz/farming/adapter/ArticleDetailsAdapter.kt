package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.ArticleDetailsLayoutBinding
import com.shahbaz.farming.databinding.ArticleLayoutBinding
import com.shahbaz.farming.datamodel.Article
import com.shahbaz.farming.datamodel.article.Data

class ArticleDetailsAdapter() : RecyclerView.Adapter<ArticleDetailsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(val binding: ArticleDetailsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Data?) {
            currentItem?.let {
                binding.apply {
                    descTextxArticleListFrag.text = it.attributes.name
                    Glide.with(itemView).load(it.attributes.main_image_path)
                        .into(imageArticleListFrag)
                }
            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ArticleDetailsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    var onItemClick: ((Data?) -> Unit)? = null
}