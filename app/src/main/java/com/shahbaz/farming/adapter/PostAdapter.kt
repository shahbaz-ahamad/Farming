package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.PostItemBinding
import com.shahbaz.farming.datamodel.Post
import java.util.concurrent.TimeUnit

class PostAdapter : RecyclerView.Adapter<PostAdapter.postViewmodel>() {
    class postViewmodel(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postItem: Post?) {
            postItem?.let {
                binding.apply {
                    title.text = postItem.title
                    description.text = postItem.description
                    //bind image
                    Glide.with(itemView).load(postItem.image).into(postImage)
                    profileName.text=postItem.userName
                    Glide.with(itemView).load(postItem.userProfile).into(profileImage)

                    //posttime
                    postTime.text = getTimeAgo(postItem.timeStamp)
                }
            }
        }

    }

    private val diffutil = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewmodel {
        return postViewmodel(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: postViewmodel, position: Int) {
        val postItem = differ.currentList[position]
        holder.bind(postItem)
    }


   companion object{
       fun getTimeAgo(postTimeInMillis: Long): String {
           val currentTimeInMillis = System.currentTimeMillis()
           val diffInMillis = currentTimeInMillis - postTimeInMillis

           return when {
               diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
               diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                   val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                   "$minutes minute${if (minutes != 1L) "s" else ""} ago"
               }
               diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                   val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                   "$hours hour${if (hours != 1L) "s" else ""} ago"
               }
               else -> {
                   val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                   "$days day${if (days != 1L) "s" else ""} ago"
               }
           }
       }
   }
}