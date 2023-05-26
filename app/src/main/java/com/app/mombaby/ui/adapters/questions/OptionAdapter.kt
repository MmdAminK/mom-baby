package com.app.mombaby.ui.adapters.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.OptionItemBinding
import com.app.mombaby.models.adapterModels.Option
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener

class OptionAdapter(private val options: ArrayList<Option>) :
    RecyclerView.Adapter<OptionAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    lateinit var binding: OptionItemBinding

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = OptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position], onItemClickListener!!)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    class ViewHolder(val binding: OptionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: Option?, onItemClickListener: OnItemClickListener) {
            binding.option = option
            binding.root.setOnClickListener { onItemClickListener.onItemClick(adapterPosition) }
            binding.executePendingBindings()
        }

    }
}
