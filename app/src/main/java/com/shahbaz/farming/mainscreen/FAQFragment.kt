package com.shahbaz.farming.mainscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.FAQAdapter
import com.shahbaz.farming.databinding.FragmentFAQBinding
import com.shahbaz.farming.datamodel.FAQ
import com.shahbaz.farming.util.hideBottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FAQFragment : Fragment() {

    private lateinit var binding: FragmentFAQBinding
    private val faqAdapter by lazy {
        FAQAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFAQBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(requireContext())
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

    private fun setupRecyclerView(context: Context) {
        val questionAnswerList = listOf(
            FAQ(
                1,
                context.getString(R.string.faq_question_1),
                context.getString(R.string.faq_answer_1)
            ),
            FAQ(
                2,
                context.getString(R.string.faq_question_2),
                context.getString(R.string.faq_answer_2)
            ),
            FAQ(
                3,
                context.getString(R.string.faq_question_3),
                context.getString(R.string.faq_answer_3)
            ),
            FAQ(
                4,
                context.getString(R.string.faq_question_4),
                context.getString(R.string.faq_answer_4)
            )
        )
        binding.faqRecyclerView.apply {
            adapter = faqAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            faqAdapter.differ.submitList(questionAnswerList)
        }
    }

}