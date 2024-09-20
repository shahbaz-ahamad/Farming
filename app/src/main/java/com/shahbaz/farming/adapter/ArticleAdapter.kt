package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.databinding.ArticleLayoutBinding
import com.shahbaz.farming.datamodel.Article

class ArticleAdapter() : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(val binding: ArticleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Article?) {
            currentItem?.let {
                binding.apply {
                    descTextxArticleListFrag.text = it.title
                    imageArticleListFrag.setImageResource(it.image)
                }
            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ArticleLayoutBinding.inflate(
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
            onItemClick?.invoke(currentItem.title.lowercase())
        }
    }

    var onItemClick: ((String) -> Unit)? = null
}