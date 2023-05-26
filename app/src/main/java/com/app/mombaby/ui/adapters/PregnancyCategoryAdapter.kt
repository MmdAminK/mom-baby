package com.app.mombaby.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.PregnancyCategoryItemBinding
import com.app.mombaby.models.pregnancyHome.categories.PregnancyCategory
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.utils.animations.AnimUtils.animatedVisibility

class PregnancyCategoryAdapter(private val pregnancyCategories: List<PregnancyCategory>) :
    RecyclerView.Adapter<PregnancyCategoryAdapter.ViewHolder>() {

    init {
        (pregnancyCategories as ArrayList).add(
            0,
            PregnancyCategory("0", "همه موارد", isSelected = true)
        )
    }

    var onItemClickListener: OnItemClickListener? = null
    lateinit var binding: PregnancyCategoryItemBinding

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            PregnancyCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pregnancyCategories[position], onItemClickListener!!)
    }

    override fun getItemCount(): Int {
        return pregnancyCategories.size
    }

    class ViewHolder(val binding: PregnancyCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pregnancyCategory: PregnancyCategory?, onItemClickListener: OnItemClickListener) {
            binding.categoryName = pregnancyCategory!!.title
            binding.root.setOnClickListener { onItemClickListener.onItemClick(adapterPosition) }

            binding.categoryLinear.animatedVisibility({
                if (pregnancyCategory.isSelected)
                    binding.categoryLinear.setBackgroundColor(Color.parseColor("#FFDAD4"))
                else
                    binding.categoryLinear.setBackgroundColor(Color.WHITE)
            }, 120)

            binding.executePendingBindings()
        }
    }
}
