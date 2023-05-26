package com.app.mombaby.ui.fragments.mainPage.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mombaby.R
import com.app.mombaby.databinding.ShowBlogsBinding
import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import com.app.mombaby.ui.adapters.PregnancyCategoryAdapter
import com.app.mombaby.ui.adapters.ShowBlogsAdapter
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.ShowBlogsEvent
import com.app.mombaby.viewModels.ShowBlogsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowBlogs : Fragment() {

    lateinit var binding: ShowBlogsBinding
    lateinit var pregnancyCategoryAdapter: PregnancyCategoryAdapter
    lateinit var showBlogsAdapter: ShowBlogsAdapter

    var adapterBlogs: ArrayList<PregnancyBlog>? = ArrayList()
    var allBlogs: ArrayList<PregnancyBlog>? = ArrayList()
    val viewModel: ShowBlogsViewModel by viewModels()


    var title: String = ""
    private var catId: Int = 0
    var week: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = requireArguments().getString("title").toString()
        week = requireArguments().getInt("week")

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.show_blogs, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showBlogsActionBar.onBackClickListener { findNavController().popBackStack() }
        binding.showBlogsActionBar.setTitle(title)

        if (week > 0) {
            viewModel.setStateEvent(ShowBlogsEvent.PregnancyGuideCategories(week))
            viewModel.dataStateGuideCategories.observe(viewLifecycleOwner, { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                    }
                    is DataState.Success -> {
                        val categories = dataState.data!!.guidesCategory
                        pregnancyCategoryAdapter =
                            PregnancyCategoryAdapter(categories)

                        val layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            true
                        ).apply { stackFromEnd = false }

                        binding.showBlogsCategoryRecyclerView.layoutManager = layoutManager
                        pregnancyCategoryAdapter.onItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                if (adapterBlogs != null) {
                                    categories.mapIndexed { index, pregnancyCategory ->
                                        pregnancyCategory.isSelected = index == position
                                    }

                                    adapterBlogs!!.clear()
                                    if (position == 0)
                                        adapterBlogs!!.addAll(allBlogs!!)
                                    else
                                        adapterBlogs!!.addAll(allBlogs!!.filter { blog -> blog.categoryId == categories[position].id })


                                    showBlogsAdapter.notifyDataSetChanged()
                                    pregnancyCategoryAdapter.notifyDataSetChanged()
                                }
                            }

                        })
                        binding.showBlogsCategoryRecyclerView.adapter = pregnancyCategoryAdapter
                    }
                    is DataState.RetrofitError -> {
                        binding.root.showInternetWarningToast()
                    }
                    is DataState.Error -> {
                        binding.root.showInternetWarningToast()
                    }
                }
            })

            viewModel.setStateEvent(ShowBlogsEvent.PregnancyGuideBlogs(catId, week))
            viewModel.dataStateGuideBlogs.observe(viewLifecycleOwner, { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        adapterBlogs!!.clear()
                        allBlogs!!.clear()
                        adapterBlogs!!.addAll(dataState.data!!.guides as ArrayList<PregnancyBlog>)
                        allBlogs!!.addAll(dataState.data.guides as ArrayList<PregnancyBlog>)

                        showBlogsAdapter = ShowBlogsAdapter(adapterBlogs!!)
                        binding.showBlogsBlogsRecyclerView.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        showBlogsAdapter.onItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                viewModel.setStateEvent(ShowBlogsEvent.SingleGuide(adapterBlogs!![position].id))
                                viewModel.dataStateSingleGuide.observe(viewLifecycleOwner, {
                                    when (it) {
                                        is DataState.Error -> {
                                            binding.root.showInternetWarningToast()
                                        }
                                        DataState.Loading -> {
                                        }
                                        is DataState.RetrofitError -> {
                                            binding.root.showInternetWarningToast()
                                        }
                                        is DataState.Success -> {
                                            val blog = adapterBlogs!![position]
                                            blog.view = it.data?.blog?.view!!
                                            val bundle = bundleOf("blog" to blog)
                                            findNavController().navigate(
                                                R.id.action_showBlogs_to_blogItemDetail,
                                                bundle
                                            )
                                        }
                                    }

                                })
                            }
                        })
                        binding.showBlogsBlogsRecyclerView.adapter = showBlogsAdapter
                    }
                    is DataState.Error -> {
                        binding.root.showInternetWarningToast()
                    }
                    DataState.Loading -> {
                    }
                    is DataState.RetrofitError -> {
                        binding.root.showInternetWarningToast()
                    }
                }

            })

        } else {
            viewModel.setStateEvent(ShowBlogsEvent.PregnancyArticleCategories)
            viewModel.dataStateArticleCategories.observe(viewLifecycleOwner, { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                    }
                    is DataState.Success -> {
                        val categories = dataState.data!!.articleCategory
                        pregnancyCategoryAdapter =
                            PregnancyCategoryAdapter(categories)

                        val layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            true
                        ).apply { stackFromEnd = false }

                        binding.showBlogsCategoryRecyclerView.layoutManager = layoutManager
                        pregnancyCategoryAdapter.onItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                if (adapterBlogs != null) {
                                    categories.mapIndexed { index, pregnancyCategory ->
                                        pregnancyCategory.isSelected = index == position
                                    }

                                    adapterBlogs!!.clear()

                                    if (position == 0)
                                        adapterBlogs!!.addAll(allBlogs!!)
                                    else
                                        adapterBlogs!!.addAll(allBlogs!!.filter { blog -> blog.categoryId == categories[position].id })

                                    showBlogsAdapter.notifyDataSetChanged()
                                    pregnancyCategoryAdapter.notifyDataSetChanged()
                                }
                            }

                        })
                        binding.showBlogsCategoryRecyclerView.adapter = pregnancyCategoryAdapter
                    }
                    is DataState.RetrofitError -> {
                        binding.root.showInternetWarningToast()
                    }
                    is DataState.Error -> {
                        binding.root.showInternetWarningToast()
                    }
                }
            })

            viewModel.setStateEvent(ShowBlogsEvent.PregnancyArticleBlogs(catId))
            viewModel.dataStateArticleBlogs.observe(viewLifecycleOwner, { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        adapterBlogs!!.clear()
                        allBlogs!!.clear()
                        adapterBlogs!!.addAll(dataState.data!!.articles as ArrayList<PregnancyBlog>)
                        allBlogs!!.addAll(dataState.data.articles as ArrayList<PregnancyBlog>)
                        showBlogsAdapter = ShowBlogsAdapter(adapterBlogs!!)
                        binding.showBlogsBlogsRecyclerView.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        showBlogsAdapter.onItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                viewModel.setStateEvent(ShowBlogsEvent.SingleArticle(adapterBlogs!![position].id))
                                viewModel.dataStateSingleArticle.observe(viewLifecycleOwner, {
                                    when (it) {
                                        is DataState.Error -> {
                                            binding.root.showInternetWarningToast()
                                        }
                                        DataState.Loading -> {
                                        }
                                        is DataState.RetrofitError -> {
                                            binding.root.showInternetWarningToast()
                                        }
                                        is DataState.Success -> {
                                            val blog = adapterBlogs!![position]
                                            blog.view = it.data?.blog?.view!!
                                            val bundle = bundleOf("blog" to it.data.blog)
                                            findNavController().navigate(
                                                R.id.action_showBlogs_to_blogItemDetail,
                                                bundle
                                            )
                                        }
                                    }
                                })
                            }

                        })
                        binding.showBlogsBlogsRecyclerView.adapter = showBlogsAdapter
                    }
                    is DataState.Error -> {
                        binding.root.showInternetWarningToast()
                    }
                    DataState.Loading -> {
                    }
                    is DataState.RetrofitError -> {
                        binding.root.showInternetWarningToast()
                    }
                }

            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearObservers()
    }

}