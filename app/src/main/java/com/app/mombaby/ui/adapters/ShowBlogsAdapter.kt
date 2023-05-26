package com.app.mombaby.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.BlogItemBinding
import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.utils.utilities.DateParser

class ShowBlogsAdapter(private val blogs: List<PregnancyBlog>) :
    RecyclerView.Adapter<ShowBlogsAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    lateinit var binding: BlogItemBinding

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = BlogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blogs[position], onItemClickListener!!)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    class ViewHolder(val binding: BlogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pregnancyBlog: PregnancyBlog?, onItemClickListener: OnItemClickListener) {
            binding.pregnancyBlog = pregnancyBlog
            binding.blogItemTimeCreated.text =
                DateParser.changeSeparatorSign(pregnancyBlog!!.timeCreate!!)
            binding.root.setOnClickListener { onItemClickListener.onItemClick(adapterPosition) }

            binding.executePendingBindings()
        }

    }
}
