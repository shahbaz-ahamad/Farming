package com.shahbaz.farming.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.databinding.ChatItemBotBinding
import com.shahbaz.farming.databinding.ChatItemUserBinding
import com.shahbaz.farming.datamodel.Message

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()

    companion object {
        const val VIEW_TYPE_USER = 0
        const val VIEW_TYPE_BOT = 1
    }

    inner class UserViewHolder(val binding: ChatItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.userMessage.text = message.text
        }
    }

    inner class BotViewHolder(val binding: ChatItemBotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.botMessage.text = message.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ChatItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            UserViewHolder(binding)
        } else {
            val binding = ChatItemBotBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            BotViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is BotViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isBot) VIEW_TYPE_BOT else VIEW_TYPE_USER
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
