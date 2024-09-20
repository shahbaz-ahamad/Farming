package com.shahbaz.farming.mainscreen

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.shahbaz.farming.adapter.ChatAdapter
import com.shahbaz.farming.databinding.FragmentChatBinding
import com.shahbaz.farming.datamodel.Message
import com.shahbaz.farming.util.Constant.Companion.GOOGLE_GEMINI_API_KEY
import com.shahbaz.farming.util.hideBottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            // If the keypad is shown (height > 200), move up the layout
            if (keypadHeight > 200) {
                binding.bottomLayout.translationY = (-keypadHeight).toFloat()
            } else {
                // Reset to default position when the keyboard is hidden
                binding.bottomLayout.translationY = 0f
            }
        }

        setupRecyclerView()

        binding.sendButton.setOnClickListener {
            val prompt = binding.messageBox.text.toString().trim()
            if (prompt.isNotEmpty()) {
                sendMessage(prompt)
            }
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun sendMessage(prompt: String) {
        // Add user message to chat
        chatAdapter.addMessage(Message(prompt, isBot = false))
        // Clear the input box
        binding.messageBox.text.clear()

        // Send the prompt to the Generative Model API
        val generateModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = GOOGLE_GEMINI_API_KEY
        )

        runBlocking {
            val response = withContext(Dispatchers.IO) {
                generateModel.generateContent(prompt)

            }
            Log.d("ChatFragment", "Response: $response")

            // Add bot response to chat
            chatAdapter.addMessage(Message(response.text.toString(), isBot = true))

            // Scroll to the latest message
            binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }
}
