package com.shahbaz.farming.mainscreen.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.ArticleDetailsAdapter
import com.shahbaz.farming.databinding.FragmentArticleDetailsBinding
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ArticleDetailsFragment : Fragment() {

    private lateinit var binding: FragmentArticleDetailsBinding
    private val articleViewmodel by viewModels<ArticleViewModel>()
    private val articleDetailsAdapter by lazy {
        ArticleDetailsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        val name = arguments?.getString("name")
        if (name != null) {
            Log.d("name", name)
            articleViewmodel.getArticle(name)
        }
        observeArticle()
        articleDetailsAdapter.onItemClick = {
            it?.let { data ->
                val action =
                    ArticleDetailsFragmentDirections.actionArticleDetailsFragmentToSpecificElementDetailsFragment(
                        data
                    )
                findNavController().navigate(action)
            }

        }

    }

    private fun observeArticle() {
        lifecycleScope.launch {
            articleViewmodel.article_state.collect {
                when (it) {

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("fetched Data", it.data.toString())
                        articleDetailsAdapter.differ.submitList(it.data?.data)

                    }

                    else -> Unit
                }
            }
        }
    }

    fun setUpRecyclerView() {
        binding.recyclerviewDArticle.apply {
            adapter = articleDetailsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

}